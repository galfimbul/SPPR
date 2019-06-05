package sample.controllers;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import sample.Main;


public class ResultWindowController {
    double[] finalyResult; // массив с весами альтернатив
    double value = 0;
    double nvpKritValue = 0; // значение НВП критериев
    double nvpAlterValue = 0;// значение элемента НВП альтернатив
     private static String[][] listView; // список с альтернативами и весами в интерфейсе (можно заменить на HashMap)

    public static String[][] getListView() {
        return listView;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> result_Window_List;

    @FXML
    private Button result_Window_newBtn;

    @FXML
    private Button result_Window_CLoseBtn;

    @FXML
    private MenuItem new_Calculation_Menu;

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
        HelloWindowController wc = new HelloWindowController();
        int size = InputKritAmountController.getAlternative_Amount(); // количество альтернатив
        finalyResult = new double[size];
        listView = new String[size][2];

        //вычисление веса каждой альтернативы и заполнение массива finalyResult
        for (int i = 1; i < MatrixWindowController.getResultMassiv().length; i++) {
            for (int j = 0; j < MatrixWindowController.getResultMassiv()[0].length; j++) {
                nvpKritValue = MatrixWindowController.getResultMassiv()[0][j];
                nvpAlterValue = MatrixWindowController.getResultMassiv()[i][j];
                value = value + (nvpKritValue * nvpAlterValue);
                //result_Window_List.getItems().add(MatrixWindowController.getResultMassiv()[i][j]);
                System.out.printf("%2.3f * %2.3f = %f, value = %2.3f \t", nvpKritValue, nvpAlterValue, nvpKritValue *
                        nvpAlterValue, value);
            }
            finalyResult[i - 1] = value;
            value = 0;
            System.out.println();
        }

        //вывод результирующего массива для проверки вычислений
        for (int i = 0; i < MatrixWindowController.getResultMassiv().length; i++) {
            for (int j = 0; j < MatrixWindowController.getResultMassiv()[0].length; j++) {
                System.out.printf("%2.3f ", MatrixWindowController.getResultMassiv()[i][j]);
            }
            System.out.println();
        }
        //заполнение массива для списка. Состоит из альтернативы и ее веса
        for (int i = 0; i < InputKritAmountController.getAlternative_Amount(); i++) {
            listView[i][0] = InputAlternativController.getInputAlternativList().get(i);
            listView[i][1] = String.format(Locale.ENGLISH,"%2.3f", finalyResult[i]);
        }
        for (final String[] s : listView) {
            System.out.println(s[0] + " + " + s[1]);
        }
        System.out.println("Длина массива: " + listView[0].length);
        // сортировка матрицы результатов
        boolean isSorted = false;
        String[] buf;
        while(!isSorted) {
            isSorted = true;
            for (int i = 0; i < finalyResult.length-1; i++) {
                if(Double.parseDouble(listView[i][1]) < Double.parseDouble(listView[i+1][1])){
                    isSorted = false;

                    buf = listView[i];
                    listView[i] = listView[i+1];
                    listView[i+1] = buf;
                }
            }
        }

        for (final String[] s : listView) {
            System.out.println(s[0] + " + " + s[1]);
        }
        for (int i = 0; i < finalyResult.length; i++) {
            result_Window_List.getItems().add(listView[i][0] + " : " + listView[i][1]);
        }

        //Кнопка новый расчет
        result_Window_newBtn.setOnAction(event -> {

            createNewSession();
            HelloWindowController.set_And_Show_Window("/sample/windows/Hello_Window.fxml");
        });
        save_Menu.setOnAction(event -> {
            HelloWindowController.saveProgram();
        });

        load_Menu.setOnAction(event -> {HelloWindowController.loadProgram();
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/sample/windows/input_Krit_Amount_Window.fxml"));
                Main.stage.setScene(new Scene(parent));
                Main.stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        about_Menu.setOnAction(event -> {
            HelloWindowController.createInformationWindow("Система поддержки принятия решений создана в рамках МКР в 2019 году","О программе");
        });
        connection_Menu.setOnAction(event -> {
            HelloWindowController.createInformationWindow("По всем возникшим вопросам обращаться: aevshvetsov@gmail.com",
                    "Связь с автором");
        });
        close_Menu.setOnAction(event -> System.exit(0));
        result_Window_CLoseBtn.setOnAction(event -> System.exit(0));
        new_Calculation_Menu.setOnAction(event -> {
            wc.set_And_Show_Window("/sample/windows/input_Krit_Amount_Window.fxml");
            createNewSession();
        });
    }
    public static void createNewSession() {
        InputKritAmountController.setKrit_Amount(0);
        InputKritAmountController.setAlternative_Amount(0);
        SelectKritController.getSelectedKritMass().clear();
        InputAlternativController.getInputAlternativList().clear();
        MatrixWindowController.getSaveMatrixValue().clear();
    }
}