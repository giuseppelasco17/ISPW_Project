

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * La classe main permette di lanciare l'applicazione, in particolare inizializza la grafica javaFX
     */

    @Override
    public void start(Stage primaryStage){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/boundary/Uc5_home.fxml"));
        primaryStage.setTitle("Ricerca prenotazione");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
