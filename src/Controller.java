import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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

    public void printButton(){
        populateListView();
    }

    public void checkInButton(){
        String title = showTitleInputDialog();
        if (title != null) {
            try {
                bookstore.checkIn(title);
            } catch (IOException e) {
                e.printStackTrace();
            }
            populateListView();
        }
    }

    public void checkOutButton() {
        String title = showTitleInputDialog();
        if (title != null) {
            try {
                bookstore.checkOut(title);
            } catch (IOException e) {
                e.printStackTrace();
            }
            populateListView();
        }
    }

    private String showTitleInputDialog() {
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.APPLICATION_MODAL);
        TextField textField = new TextField();
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> inputStage.close());

        VBox vBox = new VBox();
        vBox.getChildren().addAll(textField, submitButton);

        Scene scene = new Scene(vBox, 400, 200);
        inputStage.setScene(scene);
        inputStage.getIcons().add(new Image("img/icon.png"));
        inputStage.setTitle("Enter Title");
        inputStage.setResizable(false);
        inputStage.showAndWait();

        return textField.getText();
    }

    private void populateListView() {
        bookTable.getItems().clear();
        bookTable.getItems().addAll(bookstore.bookCollection);
    }

}