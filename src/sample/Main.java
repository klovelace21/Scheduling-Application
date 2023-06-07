package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * main class of the application
 */
public class Main extends Application {
    /**
     * initializes the login fxml file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
        primaryStage.setScene(new Scene(root, 487, 309));
        primaryStage.show();
    }

    /**
     * created the connection to the mySQL database and launches the program
     */
    public static void main(String[] args) {
        JDBC.makeConnection();


        launch(args);
    }
}
