package at.fhv.teamb.symphoniacus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;

//import static javafx.application.ConditionalFeature.FXML;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


        /*
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        /*
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
        */

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/basic_frame.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("Mailer");
            stage.setScene(scene);
            stage.show();

        }catch (IOException e){
            e.fillInStackTrace();
        }

    }
}