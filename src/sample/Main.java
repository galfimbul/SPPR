package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        //Parent root = FXMLLoader.load(getClass().getResource("windows/Hello_Window.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/sample/windows/Hello_Window.fxml"));
        primaryStage.setTitle("СППР");
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/favicon2.PNG")));

        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
