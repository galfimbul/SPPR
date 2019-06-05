package sample.controllers;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Main;

public class InputKritAmountController {
    private static int krit_Amount = 0;
    private static int alternative_Amount = 0;

    public static void setKrit_Amount(int krit_Amount) {
        InputKritAmountController.krit_Amount = krit_Amount;
    }

    public static void setAlternative_Amount(int alternative_Amount) {
        InputKritAmountController.alternative_Amount = alternative_Amount;
    }

    public static int getKrit_Amount() {
        return krit_Amount;
    }

    public static int getAlternative_Amount() {
        return alternative_Amount;
    }

    @FXML
    private Button input_Krit_Amount_NextBut;

    @FXML
    private TextArea input_Krit_Amount_TextArea;

    @FXML
    private Label input_Krit_Amount_InputKritLabel;

    @FXML
    private Label input_Krit_Amount_InputAlternativLabel;

    @FXML
    private TextField input_Krit_Amount_InputKritField;

    @FXML
    private TextField input_Krit_Amount_InputAlternativField;

    @FXML
    private MenuItem new_Calculation_Menu;

    @FXML
    private MenuItem save_Menu;

    @FXML
    private MenuItem load_Menu;

    @FXML
    private MenuItem close_Menu;

    @FXML
    private MenuItem about_Menu;

    @FXML
    private MenuItem connection_Menu;

    @FXML
    void initialize() {
        //Main.stage.setResizable(true);
        input_Krit_Amount_InputKritField.setText(krit_Amount + ""); //установка начального значения поля ввода критериев
        input_Krit_Amount_InputAlternativField.setText("" + alternative_Amount); // альтернатив
        MouseClickedOnField(input_Krit_Amount_InputKritField); // добавление слушателя на клик мышью в поле
        MouseClickedOnField(input_Krit_Amount_InputAlternativField);
        System.out.println(getClass().getSimpleName()); // узнать в каком окне находишься
        //HelloWindowController wc = new HelloWindowController(); // экземпляр объекта с методом установки сцены
        System.out.println("критериев = " + krit_Amount);
        System.out.println("Альтернатив = " + alternative_Amount);
        input_Krit_Amount_NextBut.setOnAction(event -> {
            String warning_Message_Krit_Amount = "Введите корректное значение поля " +
                    input_Krit_Amount_InputKritLabel.getText();
            String warning_Message_Alternativ_Amount = "Введите корректное значение поля "
                    + input_Krit_Amount_InputAlternativLabel.getText();
            krit_Amount = warningCheck(krit_Amount, warning_Message_Krit_Amount, input_Krit_Amount_InputKritField);
            alternative_Amount = warningCheck(alternative_Amount, warning_Message_Alternativ_Amount,
                    input_Krit_Amount_InputAlternativField);
            System.out.println("Критериев: " + krit_Amount);
            System.out.println("Альтернатив: " + alternative_Amount);
            //input_Krit_Amount_NextBut.getScene().getWindow().hide();
            if (krit_Amount == 0 || alternative_Amount == 0) {
                createAlertWindow("Укажите значение");
            } else {
                HelloWindowController.set_And_Show_Window("/sample/windows/Select_Krit_Window.fxml");
            }
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
            HelloWindowController.createInformationWindow("Система поддержки принятия решений создана в рамках МКР в " +
                    "2019 году","О программе");
        });
        connection_Menu.setOnAction(event -> {
            HelloWindowController.createInformationWindow("По всем возникшим вопросам обращаться: aevshvetsov@gmail.com",
                    "Связь с автором");
        });
    }

    private void createAlertWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Неверные значения полей");
        String s = krit_Amount == 0 ? " количества альтернатив" : " количества критериев";
        alert.setContentText(String.format(message + s));

        alert.showAndWait();
    }

    private void MouseClickedOnField(TextField textField) {
        textField.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            textField.setText("");
        });
    }

    private int warningCheck(int checked_Value, String message, TextField textField) {
        //while (checked_Value <= 0||checked_Value>10) {
        String s = textField.getText();
        if (s.matches("^((10)|[1-9])$")) {
            checked_Value = Integer.parseInt(s);
        } else checked_Value = createWarningWindow(message, checked_Value);
        //System.out.printf("%1$s = %1$s\n",checked_Value);
        return checked_Value;
    }

    private int createWarningWindow(String message, int n) {
        String s;
        boolean cycle = true;
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Ошибка");
        dialog.setHeaderText("Причина");
        dialog.setContentText(message);

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        while (cycle) {
            if (result.isPresent()) {
                s = result.get();
                if (s.matches("^((10)|[1-9])$")) {
                    n = Integer.parseInt(result.get());
                    cycle = false;

                } else {
                    result = dialog.showAndWait();
                    //dialog.getEditor().setText("");
                }
            } else {
                System.out.println("Отмена");
                cycle = false;
                n = 0;
            }

        }
        return n;
// The Java 8 way to get the response value (with lambda expression).
        //result.ifPresent(name -> System.out.println("Your name: " + name));
    }
}

