/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * October 26, 2023.
 * Controller.java
 * This class controls the main buttons, populates the table view and handles text input
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


public class Controller {

    Library bookstore = new Library();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    }

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> barcodeColumn;

    @FXML
    private TableColumn<Book, String> dueDateColumn;

    @FXML
    private TableColumn<Book, Boolean> availabilityColumn;

    @FXML
    private Pane mainPane;


    /**
     * method: checkInButton()
     * parameters: n/a
     * return: n/a
     * purpose: checkIn button action controller
     */
    public void checkInButton() throws IOException {
        String title = showTitleInputDialog();
        if (title != null) {
            bookstore.checkIn(title);
            populateListView();
        }
    }


    /**
     * method: checkOutButton()
     * parameters: n/a
     * return: n/a
     * purpose: checkOut button action controller
     */
    public void checkOutButton() throws IOException {
        String title = showTitleInputDialog();
        if (title != null) {
            bookstore.checkOut(title);
            populateListView();
        }
    }


    /**
     * method: removeTitle()
     * parameters: n/a
     * return: n/a
     * purpose: remove by title button action controller
     */
    public void removeTitle() throws IOException {
        String title = showTitleInputDialog();
        if (title != null) {
            bookstore.removeBook(title);
            populateListView();
        }
    }


    /**
     * method: removeBarcode()
     * parameters: n/a
     * return: n/a
     * purpose: remove by barcode button action controller
     */
    public void removeBarcode() throws IOException {
        int barcode;
        String title = showTitleInputDialog();
        if (title.matches("\\d+")) { // This line verifies that the string doesn't contain any letters
            barcode = Integer.parseInt(Objects.requireNonNull(showTitleInputDialog()));
            bookstore.removeBook(barcode);
            populateListView();
        }
        else{
            bookstore.errorAlert();
        }
    }


    /**
     * method: showTitleInputDialog()
     * parameters: n/a
     * return: String
     * purpose: Creates an input stage, prompts user to enter a title into a TextField and then returns
     * the inputted string.
     */
    private String showTitleInputDialog() {
        try {
            Stage inputStage = new Stage();
            inputStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlVisuals/inputStage.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            TextField textField = (TextField) scene.lookup("#inputText");
            Button submitButton = (Button) scene.lookup("#inputButton");
            submitButton.setOnAction(e -> inputStage.close());

            inputStage.setScene(scene);
            inputStage.getIcons().add(new Image("img/icon.png"));
            inputStage.setTitle("Search");
            inputStage.setResizable(false);
            inputStage.showAndWait();

            return textField.getText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * method: populateListView()
     * parameters: n/a
     * return: n/a
     * purpose: populates the table with the contents book Collection
     */
    public void populateListView() {
        bookTable.getItems().clear();
        bookTable.getItems().addAll(bookstore.bookCollection);
    }


    /**
     * method: menuOpenFile()
     * parameters: n/a
     * return: n/a
     * purpose: controls the 'Open File' menu option.
     */
    public void menuOpenFile() throws IOException {
        bookstore.openFile();
        populateListView();
    }


    /**
     * method: menuOpenFile()
     * parameters: n/a
     * return: n/a
     * purpose: controls the "Quit" menu option; closes the app.
     */
    public void menuQuitApp() {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.close();
    }

    /**
     * method: menuOpenFile()
     * parameters: n/a
     * return: n/a
     * purpose: controls the 'GitHub' menu option. Tries to open this project's GitHub repo on the PC's default browser.
     */
    public void openGithub() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/kiko-lprh/Cordero_Diego_LMS"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}