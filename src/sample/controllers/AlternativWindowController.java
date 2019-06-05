package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import sample.Main;

public class AlternativWindowController {
    static int n = InputKritAmountController.getAlternative_Amount() + 1; // количество строк матрицы альтернатив
    static int m = n - 1; // столбцы
    private double[] nvpAlternativ; // НВП после расчета матрицы
    private TextField tf; // ячейка матрицы
    private TextField textField;
    private int matrixSize = InputKritAmountController.getKrit_Amount();// количество критериев
    private int iteration = 1;
    private ObservableList<String> alternativ_Mass = InputAlternativController.getInputAlternativList();
    private double osValue;
    /*
    Начальный элемент массива сохранения результатов с которого начинаются элементы матрицы сравнения по критериям
    @param initCountValue
     */
    private int initCountValue = matrixSize*matrixSize;
    private int z = 0;



    @FXML
    private Label matrix_Window_Label;

    @FXML
    private Button matrix_Window_NextBut;

    @FXML
    private Button matrix_Window_PrevBut;

    @FXML
    private GridPane matrix_Window_MatrixPane;

    @FXML
    private Label matrix_Window_Lmax_Label;

    @FXML
    private Label matrix_Window_IS_Label;
    @FXML
    private Label matrix_Window_Os_Label;

    @FXML
    private Button alternativ_insert_Data_Btn;

    @FXML
    private Button alternativ_Clear_Matrix_Btn;

    @FXML
    private MenuItem load_Menu;

    @FXML
    private MenuItem save_Menu;

    @FXML
    private MenuItem close_Menu;

    @FXML
    private MenuItem about_Menu;

    @FXML
    private MenuItem connection_Menu;

    @FXML
    void initialize() {
        //int z = (iteration-1)*m*m;
        matrix_Window_Label.setText(String.format("Сравнение альтернатив по критерию: %s",
                SelectKritController.getSelectedKritMass().get(iteration-1)));
        //System.out.println(matrixSize);
        createMatrix(matrix_Window_MatrixPane, n);
        if(MatrixWindowController.getSaveMatrixValue().size()> initCountValue){
            setLoadedData(m,initCountValue);
        }

        matrix_Window_NextBut.setOnAction(event -> {

            matrixHandler();
            z = (iteration-1)*m*m;
            if (MatrixWindowController.getSaveMatrixValue().size() == matrixSize*m*m+initCountValue&& iteration<=matrixSize) {
                clearMatrix();
                setLoadedData(m,  initCountValue+z);
            }
            if (iteration <= InputKritAmountController.getKrit_Amount()) {
                matrix_Window_Label.setText(String.format("Сравнение альтернатив по критерию: %s",
                        SelectKritController.getSelectedKritMass().get(iteration-1)));

            }
            if(iteration > InputKritAmountController.getKrit_Amount()){
                HelloWindowController.set_And_Show_Window("/sample/windows/Result_Window.fxml");
                Main.stage.setResizable(false);
            }
            for(String s:MatrixWindowController.getSaveMatrixValue()){
                System.out.print(s + " ");
            }


        });
        matrix_Window_PrevBut.setOnAction(event -> {
            //matrix_Window_PrevBut.getScene().getWindow().hide();
            HelloWindowController.set_And_Show_Window("/sample/windows/Input_Alternativ_Window.fxml");
        });

        alternativ_insert_Data_Btn.setOnAction(event -> setTestData(m));
        //кнопка очистить матрицу
        alternativ_Clear_Matrix_Btn.setOnAction(event -> {
            clearMatrix();
        });

        save_Menu.setOnAction(event -> {HelloWindowController.saveProgram();});
        load_Menu.setOnAction(event -> {HelloWindowController.loadProgram();
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/sample/windows/input_Krit_Amount_Window.fxml"));
                Main.stage.setScene(new Scene(parent));
                Main.stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        close_Menu.setOnAction(event -> System.exit(0));
        about_Menu.setOnAction(event -> {
            HelloWindowController.createInformationWindow("Система поддержки принятия решений создана в рамках МКР в " +
                    "2019 году","О программе");
        });
        connection_Menu.setOnAction(event -> {
            HelloWindowController.createInformationWindow("По всем возникшим вопросам обращаться: aevshvetsov@gmail.com",
                    "Связь с автором");
        });
    }

    private void matrixHandler(){
        int z = (iteration)*m*m;
        System.out.println("Предполагаемый размер массива: " + ((iteration*m*m)+initCountValue));
        if (isFlag()) {

            osValue = matrixColculation(matrix_Window_MatrixPane, m);
            if (osValue * 100 <= 100) {
                for (int i = 0; i < nvpAlternativ.length; i++) {
                    MatrixWindowController.getResultMassiv()[i + 1][iteration - 1] = nvpAlternativ[i];
                    System.out.printf("%2.3f ", MatrixWindowController.getResultMassiv()[i + 1][iteration - 1]);
                }
                System.out.println();
                for (int i = 1 + m * 2; i < matrix_Window_MatrixPane.getChildren().size(); i++) {
                    textField = (TextField) matrix_Window_MatrixPane.getChildren().get(i);
                    //System.out.println("Значение текстового поля: "+ textField.getText());
                    if(MatrixWindowController.getSaveMatrixValue().size() == matrixSize*m*m + initCountValue){
                        MatrixWindowController.getSaveMatrixValue().set(z,textField.getText());
                        z++;
                    } else{MatrixWindowController.getSaveMatrixValue().add(textField.getText());}
                    System.out.println("Добавляемое значение в массив: " + textField.getText());
                }
                iteration++;
            } else
                matrixCreateAlertWindow("Значение ОС более 15%%. Заполните матрицу заново.");
        }
        if (MatrixWindowController.getSaveMatrixValue().size() < m * m * iteration + matrixSize * matrixSize)
            clearMatrix();

    }


    public void matrixCreateAlertWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(String.format(message));
        alert.showAndWait();
        clearMatrix();
    }
    private void clearMatrix() {
        for (int i = 1 + m * 2; i < matrix_Window_MatrixPane.getChildren().size(); i++) {
            textField = (TextField) matrix_Window_MatrixPane.getChildren().get(i);
            textField.setText("");
        }
    }

    private boolean isFlag() {
        boolean flag = false;
        int count = 0;
        for (int i = 1 + m * 2; i < matrix_Window_MatrixPane.getChildren().size(); i++) {
            textField = (TextField) matrix_Window_MatrixPane.getChildren().get(i);
            if (textField.getText().isEmpty() || !textField.getText().matches("^(1/[3-9])|[1-9]$")) {
                SelectKritController.createAlertWindow("Введите корректные значения матрицы");
                i = 10000;
            } else count++;
            if(count==InputKritAmountController.getAlternative_Amount()*InputKritAmountController.getAlternative_Amount())
                flag=true;
        }
        return flag;
    }

    private TextField createTextField(String text, boolean edit) {
        tf = new TextField();
        tf.minHeight(50);
        tf.minWidth(50);
        tf.setAlignment(Pos.CENTER);
        tf.setText(text);
        tf.setEditable(edit);
        GridPane.setMargin(tf, new Insets(10));
        return tf;
    }

    private void columnConstraintsCreation(GridPane matrix, int n) {
        for (int i = 0; i < n - 1; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100 / n);
            matrix.getColumnConstraints().add(cc);
        }
    }

    private void rowConstrCreation(GridPane matrix, int n) {
        for (int i = 0; i < n; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100 / n);
            matrix.getRowConstraints().add(rc);
        }
    }

    private void createMatrix(GridPane matrix, int n) {
        columnConstraintsCreation(matrix, n);
        rowConstrCreation(matrix, n);
        // Iterate the Index using the loops
        for (int i = 1; i < n; i++) {
            matrix.add(createTextField(alternativ_Mass.get(i - 1), false), i, 0);
            matrix.add(createTextField(alternativ_Mass.get(i - 1), false), 0, i);
        }
        for (int y = 1; y < n; y++) {
            for (int x = 1; x < n; x++) {
                // Create a new TextField in each Iteration
                createTextField("0", true);
                // Iterate the Index using the loops
                matrix.setRowIndex(tf, y);
                matrix.setColumnIndex(tf, x);
                tf.setMinHeight(10);
                tf.setMinWidth(100);
                matrix.getChildren().add(tf);
                GridPane.setMargin(tf, new Insets(10));
            }
        }
    }
    private void setLoadedData(int n, int count){
        int k = 1 + n*2;
        for (int j = 0; j < n*n; j++) {
            textField = (TextField) matrix_Window_MatrixPane.getChildren().get(k);
            textField.setText(MatrixWindowController.getSaveMatrixValue().get(j+count));
            k++;
        }
    }

    private void setTestData(int n) {
        int k = 1 + n * 2;
        String[][] testMatrix = new String[n][n];
        String[] values = {"1", "3", "5", "7", "9"};
        String value = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                value = getRandom(values);
                if (i == j) {
                    value = "1";
                    testMatrix[i][j] = value;
                }
                if (testMatrix[i][j] == null) {
                    testMatrix[i][j] = value;
                    if (!value.equals("1"))
                        testMatrix[j][i] = "1/" + value;
                    else testMatrix[j][i] = value;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                textField = (TextField) matrix_Window_MatrixPane.getChildren().get(k);
                textField.setText(testMatrix[i][j]);
                k++;
            }
        }
    }

    private String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public double matrixColculation(GridPane matrix, int matrixSize) { // Разобраться с методом
        int k = 1 + matrixSize * 2; // Первый индекс нужной ячейки матрицы
        double power = matrixSize; // Степень корня
        double number = 0; // ячейка строки
        String fieldValue; // значение в ячейке введенное пользователем
        double summ = 0; // сумма средних геометрических
        double product = 1; // произведение элементов строки матрицы
        double srGemValue; // среднее геометрическое строки
        ArrayList<Double> sredGeom = new ArrayList<>();// список средних геометрических строки
        double[] nvp = new double[matrixSize]; // НВП
        double summElemStolb = 0; // сумма элементов столбца матрицы
        double[] prodSummNVP = new double[nvp.length]; // произведение суммы столбца на компонент НВП
        double[] pSS = {0, 0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49}; // Показатель случайной согласованности
        double[][] digitMatrix = new double[matrixSize][matrixSize]; // цифровая матрига из значений полей
        double lMax = 0; //собственное значение матрицы
        double iS = 0; // индекс согласованности
        double oS = 0; // отношение согласованности

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                textField = (TextField) matrix.getChildren().get(k);
                fieldValue = textField.getText();
                if (fieldValue.matches("\\d/\\d")) {
                    number = (double) (fieldValue.charAt(0) - 48) / (fieldValue.charAt(2) - 48);
                    digitMatrix[i][j] = number;
                } else {
                    number = Integer.parseInt(fieldValue);

                    digitMatrix[i][j] = number;
                }
                product = product * number;
                k++;
                System.out.print(digitMatrix[i][j] + "\t");
            }
            System.out.println();
            srGemValue = Math.pow(product, 1 / power);
            sredGeom.add(srGemValue);
            summ = summ + srGemValue;
            product = 1;

        }
        //System.out.printf("сумма средних геометрических: %2.5f \n", summ);
        for (int i = 0; i < sredGeom.size(); i++) {
            nvp[i] = sredGeom.get(i) / summ;
            //System.out.printf("Компонент вектора НВП %d строки: %f\n", i + 1, nvp[i]);
        }
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                summElemStolb += digitMatrix[j][i];
            }
            //System.out.println("Сумма элементов " + (i + 1) + " столбца: " + summElemStolb);
            prodSummNVP[i] = summElemStolb * nvp[i];
            /*System.out.println("Произведение элементов " + (i + 1) + " столбца на: " + (i + 1) + " компонент НВП: " +
                    (double) prodSummNVP[i]);*/
            summElemStolb = 0;
            lMax += prodSummNVP[i];
        }
        //System.out.println("lMax: " + lMax);
        iS = ((lMax - matrixSize) / (matrixSize - 1));
        //System.out.println(" lmax- matrixSize = " + (lMax - matrixSize));
        //System.out.println(" matrixSize-1 = " + (matrixSize - 1));
        //System.out.println("iS: " + iS);
        if(pSS[matrixSize-1]==0){
            oS = 0;
        }
        else
        oS = iS / pSS[matrixSize - 1];
        //System.out.println("pSS[matrixSize] = " + pSS[matrixSize - 1]);
        System.out.println("oS: " + oS * 100 + "%");
        matrix_Window_Lmax_Label.setText(String.format("Lmax = %2.3f", lMax));
        matrix_Window_IS_Label.setText(String.format("ИС = %2.3f", iS));
        matrix_Window_Os_Label.setText(String.format("ОС = %2.3f %%", oS * 100));
        nvpAlternativ = nvp;
        return oS;
    }
}