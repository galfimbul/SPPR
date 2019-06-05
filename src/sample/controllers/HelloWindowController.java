package sample.controllers;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Main;

public class HelloWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem new_Calculation_Menu;

    @FXML
    private MenuItem save_Menu;

    @FXML
    private MenuItem load_Menu;

    @FXML
    private MenuItem close_Menu;

    @FXML
    private Label main_Window_Info;

    @FXML
    private Button main_Window_Button;

    @FXML
    private Label main_Window_Label;

    @FXML
    private MenuItem about_Menu;

    @FXML
    private MenuItem connection_Menu;

    @FXML
    void initialize() {


        System.out.println(getClass().getSimpleName());

        main_Window_Button.setOnAction(event -> {
            //main_Window_Button.getScene().getWindow().hide();
            Main.stage.setResizable(false);
            set_And_Show_Window("/sample/windows/input_Krit_Amount_Window.fxml");

            //set_And_Show_Window("/sample/windows/Matrix_Window.fxml",event);

        });
        new_Calculation_Menu.setOnAction(event -> {
            ResultWindowController.createNewSession();
        });
        close_Menu.setOnAction(event -> System.exit(0));
        save_Menu.setOnAction(event -> {
            saveProgram();
        });
        load_Menu.setOnAction(event -> {loadProgram();
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/sample/windows/input_Krit_Amount_Window.fxml"));
                Main.stage.setScene(new Scene(parent));
                Main.stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        about_Menu.setOnAction(event -> {
            createInformationWindow("Система поддержки принятия решений создана в рамках МКР в 2019 году","О программе");
        });
        connection_Menu.setOnAction(event -> {
            createInformationWindow("По всем возникшим вопросам обращаться: aevshvetsov@gmail.com", "Связь с автором");
        });

    }
    public static void createInformationWindow(String s, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
    private static String createSaveFileWindow() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyy_HH.mm");
        String date = df.format(new Date());
        FileChooser fc = new FileChooser();
        fc.setTitle("Сохранить");
        fc.setInitialFileName("Результаты расчета_" + date);
        fc.setInitialDirectory(new File(System.getProperty("user.home")+"/Documents"));
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Save Files (*.ssppr)", "*.ssppr");//Расширение
        fc.getExtensionFilters().add(extFilter);
        String s = "";
        try {
            s = fc.showSaveDialog(null).getAbsolutePath();
        }
        catch (NullPointerException e){
            s = "";
        }

        System.out.println(s);
        return s;
    }
    private static String createLoadFileWindow() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyy_HH.mm");
        String date = df.format(new Date());
        FileChooser fc = new FileChooser();
        fc.setTitle("Сохранить");
        fc.setInitialFileName("Результаты расчета_" + date);
        fc.setInitialDirectory(new File(System.getProperty("user.home")+"/Documents"));
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Save Files (*.ssppr)", "*.ssppr");//Расширение
        fc.getExtensionFilters().add(extFilter);
        String s = "";
        try {

            s = fc.showOpenDialog(null).getAbsolutePath();
        }
        catch (NullPointerException e){
            s = "";
        }

        System.out.println(s);
        return s;
    }
    public static void loadProgram(){
        ResultWindowController.createNewSession();
        int size=0; // размер массивов
        String fileName = createLoadFileWindow();
        if(fileName.isEmpty())
        System.out.println("Файл не выбран");
        if(!fileName.isEmpty()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                InputKritAmountController.setKrit_Amount(Integer.parseInt(br.readLine()));
                InputKritAmountController.setAlternative_Amount(Integer.parseInt(br.readLine()));

                size = Integer.parseInt(br.readLine());
                for (int i = 0; i < size; i++) {
                    SelectKritController.getSelectedKritMass().add(br.readLine());
                }
                size = Integer.parseInt(br.readLine());
                SelectKritController.getOptions().clear();
                for (int i = 0; i < size; i++) {
                    SelectKritController.getOptions().add(br.readLine());
                }
                size = Integer.parseInt(br.readLine());
                for (int i = 0; i < size; i++) {
                    InputAlternativController.getInputAlternativList().add(br.readLine());
                }
                size = Integer.parseInt(br.readLine());
                for (int i = 0; i < size; i++) {
                    MatrixWindowController.getSaveMatrixValue().add(br.readLine());
                    System.out.print(MatrixWindowController.getSaveMatrixValue().get(i));
                }
                System.out.println();
                size = Integer.parseInt(br.readLine());
                br.close();
                System.out.println(InputKritAmountController.getKrit_Amount());
                System.out.println(InputKritAmountController.getAlternative_Amount());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void saveProgram() {
        String fileName = createSaveFileWindow();
        if (fileName.isEmpty())
            System.out.println("Файл не сохранен");
        if(!fileName.isEmpty()) {
            try {
                FileWriter fw = new FileWriter(fileName);
                // Считываем количество критериев и альтернатив и пишем их в файл
                fw.write(String.format("%d\n", InputKritAmountController.getKrit_Amount()));
                fw.write(String.format("%d\n", InputKritAmountController.getAlternative_Amount()));
                // создаем массив критериев который будем сохранять
                ObservableList<String> saveSelectedKritMass = SelectKritController.getSelectedKritMass();
                //пишем его размер
                fw.write(String.format("%d\n", saveSelectedKritMass.size()));
                //пишем сам массив
                for (String s : saveSelectedKritMass) {
                    fw.write(String.format("%s\n", s));
                }

                //для записи выпадающего списка
                ObservableList<String> saveSelectKritList = SelectKritController.getOptions();
                fw.write(String.format("%d\n", saveSelectKritList.size()));
                for (String s : saveSelectKritList) {
                    fw.write(String.format("%s\n", s));
                }
                // для записи введенных альтернатив
                ObservableList<String> saveInputAlternativeList = InputAlternativController.getInputAlternativList();
                fw.write(String.format("%d\n", saveInputAlternativeList.size()));
                for (String s : saveInputAlternativeList) {
                    fw.write(String.format("%s\n", s));
                }
                // для записи массива с данными всех матриц
                fw.write(String.format("%d\n", MatrixWindowController.getSaveMatrixValue().size()));
                System.out.println("Размер массива с матрицами: " + MatrixWindowController.getSaveMatrixValue().size());
                for (String s : MatrixWindowController.getSaveMatrixValue()) {
                    fw.write(String.format("%s\n", s));
                }
                fw.write(String.format("%d\n", ResultWindowController.getListView().length));
                for (int i = 0; i < InputKritAmountController.getAlternative_Amount(); i++) {
                    fw.write(String.format("%s\n", ResultWindowController.getListView()[i][0]));
                    fw.write(String.format("%s\n", ResultWindowController.getListView()[i][1]));
                }
                fw.flush();
                fw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        public static void set_And_Show_Window (String path){
            Parent parent = null;

            try {
                parent = FXMLLoader.load(HelloWindowController.class.getResource(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(parent);
            Main.stage.hide();
            Main.stage.setScene(scene);
            Main.stage.show();
            //Main.stage.setResizable(true);
        }
    }

