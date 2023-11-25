/**
 * Main.java
 * The Main class handles the Library Management System's
 * GUI and launches the app.
 *
 * @author Diego Cordero
 * @version 1.0 Final
 * @date November 25, 2023.
 * @course CEN 3024 - Software Development 1
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;


/**
 * The Main class for the LMS.
 */
public class Main extends Application {

    /**
     * Sets the Main Stage.
     *
     * @param mainStage The stage that will be set
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
     * Launches the app.
     *
     * @param args String[] args.
     */
    public static void main(String[] args) {
        launch(args);
    }


}