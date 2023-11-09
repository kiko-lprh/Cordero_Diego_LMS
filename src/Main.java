/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * November 9, 2023.
 * Main.java
 * This class handles the LMS's GUI and launches the app.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;



public class Main extends Application {


    /**
     * method: start()
     * parameters: String[] args
     * return: n/a
     * purpose: Sets the Main Stage.
     */
    public void start(Stage mainStage){
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxmlVisuals/gui.fxml")));
            mainStage.setTitle("kk LMS");
            mainStage.setScene(new Scene(root));
            mainStage.setResizable(false);
            mainStage.getIcons().add(new Image("img/icon.png"));
            mainStage.show();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    /**
     * method: main()
     * parameters: String[] args
     * return: n/a
     * purpose: Launches the app.
     */
    public static void main(String[] args) {
        launch(args);

    }


}