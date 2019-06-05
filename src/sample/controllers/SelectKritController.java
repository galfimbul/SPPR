package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class SelectKritController {
    int krit_Amount = InputKritAmountController.getKrit_Amount();
    private String indexListSelectedKrit;

    public static ObservableList<String> getSelectedKritMass() {
        return selectedKritMass;
    }

    private int selectKritListSize;

    public static void setSelectedKritMass(ObservableList<String> selectedKritMass) {
        SelectKritController.selectedKritMass = selectedKritMass;
    }

    private static ObservableList<String> options =
            FXCollections.observableArrayList("Надежность", "Пропускная способность","Количество портов"
                    ,"Цена","Простота обслуживания","Наличие неоригинальных комплектующих"
                    ,"Диапазон работы","Скорость коммутации","Задержка передачи кадров"
                    ,"Поддерживаемые протоколы маршрутизации");

    public static ObservableList<String> getOptions() {
        return options;
    }

    public static void setOptions(ObservableList<String> options) {
        SelectKritController.options = options;
    }

    private static ObservableList<String> selectedKritMass =FXCollections.observableArrayList();

    @FXML
    private Label select_Krit_Window_InsertLabel;

    @FXML
    private Button select_Krit_Window_InsertBut; // кнопка добавить

    @FXML
    private Label select_Krit_Window_SelectKritLabel;

    @FXML
    private ComboBox<String> select_Krit_Window_ComboBox; // список критериев

    @FXML
    private Label select_Krit_Window_HelpLabel;

    @FXML
    private  ListView<String> select_Krit_Window_SelectedKritList; // список выбранных критериев

    @FXML
    private Label select_Krit_Window_SelectedKritLabel;

    @FXML
    private Button select_Krit_Window_PrevBut;

    @FXML
    private Button select_Krit_Window_NextBut;

    @FXML
    private Button select_Krit_Window_ListItemDeleteBut;

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
        select_Krit_Window_SelectedKritList.setItems(selectedKritMass);
        System.out.println("Размер массива выбора критериев: "+ selectedKritMass.size());

        select_Krit_Window_ComboBox.getItems().removeAll();

        select_Krit_Window_ComboBox.getItems().addAll(options);
        // получаем модель выбора элементов
        System.out.println(getClass().getSimpleName());
        //HelloWindowController wc = new HelloWindowController();

        // Реакция на выбранный элемент списка
        select_Krit_Window_ComboBox.setOnAction(event ->{
            selectKritListSize = select_Krit_Window_SelectedKritList.getItems().size();
            String s = select_Krit_Window_ComboBox.getValue();
            if(!select_Krit_Window_SelectedKritList.getItems().contains(s)
                    && selectKritListSize<krit_Amount){
                select_Krit_Window_SelectedKritList.getItems().add(s);
                //
            }
            if (select_Krit_Window_SelectedKritList.getItems().size()==krit_Amount)
                //createAlertWindow("Выбрано необходимое количество критериев");
                HelloWindowController.createInformationWindow("Выбрано необходимое число критериев","Выбор критериев");
        });

        // слушаем выбранный элемент списка
        SelectionModel<String> langsSelectionModel = select_Krit_Window_SelectedKritList.getSelectionModel();
        langsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){
                indexListSelectedKrit = newValue;
            }
        });

        // Реакция нажатия кнопки удалить
        select_Krit_Window_ListItemDeleteBut.setOnAction(event -> {
            if(select_Krit_Window_SelectedKritList.getItems().isEmpty()){
                createAlertWindow("Не выбран ни один критерий");
            }
            select_Krit_Window_SelectedKritList.getItems().remove(indexListSelectedKrit);
        });

        // нажатие кнопки добавить
        select_Krit_Window_InsertBut.setOnAction(event ->
            createInsertWindow("Введите добавляемый критерий",select_Krit_Window_ComboBox)
        );

        // нажатие кнопки далее
        select_Krit_Window_NextBut.setOnAction(event -> {
            if (select_Krit_Window_SelectedKritList.getItems().isEmpty()
                    || select_Krit_Window_SelectedKritList.getItems().size()<krit_Amount){
                int count = krit_Amount-select_Krit_Window_SelectedKritList.getItems().size();
                createAlertWindow(String.format("Выберите дополнительно %s критерия/(ев)",count));
            }else {
                //select_Krit_Window_NextBut.getScene().getWindow().hide();
                selectedKritMass = select_Krit_Window_SelectedKritList.getItems();
                for (String s:selectedKritMass) {
                    System.out.println(s);

                }
                HelloWindowController.set_And_Show_Window("/sample/windows/Input_Alternativ_Window.fxml");
            }
        });
        select_Krit_Window_PrevBut.setOnAction(event -> {
            //select_Krit_Window_PrevBut.getScene().getWindow().hide();
            HelloWindowController.set_And_Show_Window("/sample/windows/input_Krit_Amount_Window.fxml");
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
        new_Calculation_Menu.setOnAction(event -> ResultWindowController.createNewSession());
        about_Menu.setOnAction(event -> {
            HelloWindowController.createInformationWindow("Система поддержки принятия решений создана в рамках МКР в 2019 году","О программе");
        });
        connection_Menu.setOnAction(event -> {
            HelloWindowController.createInformationWindow("По всем возникшим вопросам обращаться: aevshvetsov@gmail.com","Связь с автором");
        });
    }
    private void createInsertWindow(String message,ComboBox<String> comboBox) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Добавить критерий");
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().matches("^[А-Яа-яA-Za-z]+") && !comboBox.getItems().contains(result.get())) {
                comboBox.getItems().add(result.get());
                options.add(result.get());
                System.out.println(result.get());
            }
            else createAlertWindow("Критерий присутствует в списке или же начинается с символа");
        }
    }

    public static void createAlertWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(String.format(message));
        alert.showAndWait();
    }
}