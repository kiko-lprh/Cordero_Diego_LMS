/**
 * Controller.java
 * This class controls the main buttons, populates the table view and handles text input.
 *
 * @author Diego Cordero
 * @version 1.0 Final
 * @date November 25, 2023.
 * @course CEN 3024 - Software Development 1
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Objects;

/**
 * Controls the GUI functions of the LMS.
 */
public class Controller {


    Library bookstore = new Library();
    ObservableList<Book> resultList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        bookTable.getItems().clear();

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));

        try {
            populateListView();
        } catch (SQLException e) {
            // Handle the SQLException appropriately
            e.printStackTrace();
        }

    }

    // FXML Fields
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
    private TableColumn<Book, Boolean> volumeColumn;

    @FXML
    private Pane mainPane;


    /**
     * checkIn button action controller. Calls the checkIn(String) method in the Library Class.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    public void checkInButton() throws IOException, SQLException {
        String title = showTitleInputDialog("title");
        if (title != null) {
            bookstore.manageBook(title,"in");
            populateListView();
        }
    }


    /**
     * checkOut button action controller. Calls the checkOut(String) method in the Library Class.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    public void checkOutButton() throws IOException, SQLException {
        String title = showTitleInputDialog("title");
        if (title != null) {
            bookstore.manageBook(title,"out");
            populateListView();
        }
    }


    /**
     * Remove by title button action controller. Calls the removeBook(String) method in the Library Class.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    public void removeTitle() throws IOException, SQLException {
        String title = showTitleInputDialog("title");
        if (title != null) {
            bookstore.removeBook(title);
            populateListView();
        }
    }


    /**
     * Remove by barcode button action controller. Calls the removeBook(int) method in the Library Class.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    public void removeBarcode() throws IOException, SQLException {
        int barcode;
        String title = showTitleInputDialog("barcode");

        // This line verifies that the string doesn't contain any letters
        if (title.matches("\\d+")) {
            barcode = Integer.parseInt(Objects.requireNonNull(title));
            bookstore.removeBook(barcode);
            populateListView();
        }
        else{
            bookstore.errorAlert("Enter a valid barcode.");
        }
    }


    /**
     * Creates an input stage, prompts the user to enter a title/barcode into a TextField, and then returns
     * the inputted title/barcode.
     *
     * @param either Either "barcode" or "title" as a String.
     * @return The inputted title/barcode as a String.
     */
    private String showTitleInputDialog(String either) {
        try {
            Stage inputStage = new Stage();
            inputStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlVisuals/inputStage.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Label label = (Label) scene.lookup("#inputLabel");
            TextField textField = (TextField) scene.lookup("#inputText");
            Button submitButton = (Button) scene.lookup("#inputButton");
            submitButton.setOnAction(e -> inputStage.close());


            if (either.equals("barcode")){
                label.setText("Enter book's barcode");
            }
            else {
                label.setText("Enter book's title");
            }

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
     * Populates the table with the contents of the connected database.
     *
     * @throws SQLException If a SQL error occurs.
     */
    public void populateListView() throws SQLException {

        String query = "SELECT  * FROM books";
        Statement statement = bookstore.dbConn.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);


        while (resultSet.next()) {
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            int barcode = resultSet.getInt("barcode");
            boolean availability = resultSet.getBoolean("available");
            Date dueDate = resultSet.getDate("due date");
            int volume = resultSet.getInt("volume");
            resultList.add((new Book(title,author,barcode,volume,availability,dueDate)));
        }

        bookTable.getItems().clear();
        bookTable.getItems().addAll(resultList);
        resultList.clear();
    }


    /**
     * Controls the 'Open File' menu option. Calls the openFile method to open a file.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    public void menuOpenFile() throws IOException, SQLException {
        bookstore.openFile();
        populateListView();
    }


    /**
     * Controls the "Quit" menu option; Closes the app.
     */
    public void menuQuitApp() {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.close();
    }


    /**
     * Controls the 'GitHub' menu option. Tries to open this project's GitHub repo on the PC's default browser.
     */
    public void openGithub() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/kiko-lprh/Cordero_Diego_LMS"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}