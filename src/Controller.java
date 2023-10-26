/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * October 26, 2023.
 * Controller.java
 * This class controls the main buttons, populates the table view and handles text input
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {

    Library bookstore = new Library();

    @FXML
    public void initialize() {
        String filePath = "C:\\Users\\corde\\Desktop\\1.txt";
        bookstore.openFile(filePath);
        System.out.println(bookstore.bookCollection.get(0).toString());
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


    /**
     * method: printButton()
     * parameters: n/a
     * return: n/a
     * purpose: print button action controller
     */
    public void printButton(){
        populateListView();
    }


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
     * method: printButton()
     * parameters: n/a
     * return: n/a
     * purpose: removeButton action controller
     */
    public void removeButton() throws IOException {
        String title = showTitleInputDialog();
        if (title != null) {
            bookstore.removeBook(title);
            populateListView();
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
    private void populateListView() {
        bookTable.getItems().clear();
        bookTable.getItems().addAll(bookstore.bookCollection);
    }

}