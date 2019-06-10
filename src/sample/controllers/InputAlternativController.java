package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class InputAlternativController {

    int alternativ_Amount = InputKritAmountController.getAlternative_Amount();

    private String indexListSelectedKrit;
    private static ObservableList<String> inputAlternativList = FXCollections.observableArrayList();


    public static ObservableList<String> getInputAlternativList() {
        return inputAlternativList;
    }

    int selectedAlternativSize = 0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label input_Alternativ_Label;

    @FXML
    private MenuBar input_Alternativ_MenuBarl;

    @FXML
    private Label input_Alternativ_InsertLabel;

    @FXML
    private TextField input_Alternativ_InsertField;

    @FXML
    private ListView<String> input_Alternativ_AlternativList;

    @FXML
    private Button insert_Alternativ_Delete_Btn;

    @FXML
    private Label input_Alternativ_PrevAlternativLabel;

    @FXML
    private Button input_Alternativ_InsertBut;

    @FXML
    private Label input_Alternativ_HelpLabel;

    @FXML
    private Button input_Alternativ_PrevBut;

    @FXML
    private Button input_Alternativ_NextBut;

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
        input_Alternativ_AlternativList.setItems(getInputAlternativList());
        input_Alternativ_InsertField.setOnAction(event -> {
            addAlternativ();
        });

        input_Alternativ_InsertBut.setOnAction(event -> {
            addAlternativ();
        });

        // слушаем выбранный элемент списка
        SelectionModel<String> langsSelectionModel = input_Alternativ_AlternativList.getSelectionModel();
        langsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {
                indexListSelectedKrit = newValue;
            }
        });

        // Реакция нажатия кнопки удалить
        insert_Alternativ_Delete_Btn.setOnAction(event -> {
            if (input_Alternativ_AlternativList.getItems().isEmpty()) {
                SelectKritController.createAlertWindow("Выберите альтернативу из списка!");
            }
            input_Alternativ_AlternativList.getItems().remove(indexListSelectedKrit);
        });

        System.out.println(getClass().getSimpleName());

        //Кнопка далее
        input_Alternativ_NextBut.setOnAction(event -> {
            //input_Alternativ_NextBut.getScene().getWindow().hide();

            inputAlternativList = input_Alternativ_AlternativList.getItems();
            if (inputAlternativList.isEmpty()
                    || inputAlternativList.size() < alternativ_Amount) {
                int count = alternativ_Amount - inputAlternativList.size();
                SelectKritController.createAlertWindow(String.format("Введите дополнительно %s альтернатив/(ы)", count));
            } else {
                HelloWindowController.set_And_Show_Window("/sample/windows/Matrix_Window.fxml");
                Main.stage.setResizable(true);
            }
        });
        input_Alternativ_PrevBut.setOnAction(event -> {
            //input_Alternativ_PrevBut.getScene().getWindow().hide();
            HelloWindowController.set_And_Show_Window("/sample/windows/Select_Krit_Window.fxml");
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
        new_Calculation_Menu.setOnAction(event -> {
            HelloWindowController.set_And_Show_Window("/sample/windows/input_Krit_Amount_Window.fxml");
            ResultWindowController.createNewSession();
        });

    }
    private void addAlternativ() {
        selectedAlternativSize = input_Alternativ_AlternativList.getItems().size();
        String s = input_Alternativ_InsertField.getText();
        if (!input_Alternativ_AlternativList.getItems().contains(s)
                && selectedAlternativSize < alternativ_Amount) {
            input_Alternativ_AlternativList.getItems().add(s);
            input_Alternativ_InsertField.setText("");
        }
        if (selectedAlternativSize == alternativ_Amount) {
            SelectKritController.createAlertWindow("Введено достаточное количество альтернатив");
            input_Alternativ_InsertField.setText("");
        }
        if (input_Alternativ_AlternativList.getItems().contains("")){
            SelectKritController.createAlertWindow("Была введена пустая строка. Добавьте альтернативу заново.");
            input_Alternativ_AlternativList.getItems().remove("");
        }
    }
}
