/**
 * Library.java
 * This class manages the Library's book collection. It takes care of
 * adding, removing, checkin in and checking out books.
 *
 * @author Diego Cordero
 * @version 1.0 Final
 * @date November 25, 2023.
 * @course CEN 3024 - Software Development 1
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.io.*;


/**
 * Performs the main LMS functions.
 */
public class Library {

    DBConnector dbConn = new DBConnector();

    public Library(){}


    ArrayList<Book> bookCollection = new ArrayList<>();


    /**
     * Adds a book to the database from a file.
     *
     * @param barcode The barcode of the book.
     * @param title   The title of the book.
     * @param volume  The volume of the book.
     * @param author  The author of the book.
     */
    public void addBook(String barcode, String title, String volume, String author) {

        try {
            String query = "INSERT INTO `books` (barcode, title, volume, author, available, `due date`) VALUES (?, ?, ?, ?, '1', NULL);";
            PreparedStatement statement = dbConn.connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(barcode));
            statement.setString(2, title);
            statement.setString(3, volume);
            statement.setString(4, author);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Removes a book from the database using the book's barcode as reference.
     *
     * @param barcode The barcode of the book.
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    public void removeBook(int barcode) throws IOException, SQLException {

            String query = "DELETE FROM books WHERE barcode = '" + barcode + "'";
            Statement statement = dbConn.connection.createStatement();
            int rowsAffected = statement.executeUpdate(query);

            if (rowsAffected == 0) {
                errorAlert("Book could not be found.");
            }
            else {
                successAlert("Book successfully deleted.");
            }
    }


    /**
     * Removes a book from the database that has the inputted title.
     * If there are multiple books with the same title, prompts the user to select the specific book to be deleted.
     *
     * @param title The title of the book.
     * @throws IOException If an I/O error occurs.
     */
    public void removeBook(String title) throws IOException {
        ArrayList<Book> tempBookList = new ArrayList<>();
        try {
            String checkQuery = "SELECT COUNT(*) AS Matches FROM books WHERE title = ?";
            PreparedStatement checkStatement = dbConn.connection.prepareStatement(checkQuery);
            checkStatement.setString(1, title);
            ResultSet resultSet = checkStatement.executeQuery();
            int bookCount = 0;
            if (resultSet.next()) {
                bookCount = resultSet.getInt("Matches");
            }

            if (bookCount == 1) {
                String deleteQuery = "DELETE FROM books WHERE title = ?";
                PreparedStatement deleteStatement = dbConn.connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, title);
                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected == 0) {
                    errorAlert("Book could not be found.");
                } else {
                    successAlert("Book successfully deleted.");
                }
            } else if (bookCount > 1) {
                multipleBooks("delete", tempBookList, null, true, title);
            } else {
                errorAlert("Book could not be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorAlert("An error occurred while removing the book.");
        }
    }


    /**
     * Opens and reads the chosen file from the FileChooser.
     * If the file contains valid books, sends the books' information to addBook()
     * so that they can be added to the collection.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void openFile () throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        fileChooser.setTitle("Open File");
        Stage stage = new Stage();
        File chosenFile =fileChooser.showOpenDialog(stage);

        if(chosenFile != null){
            try {
                BufferedReader read = new BufferedReader(new FileReader(chosenFile));
                String line;

                // while loop built like this, so it doesn't stop when it encounters a blank line
                while ((line = read.readLine()) != null) {
                    if(!line.isEmpty()){
                        String[] tempArray = line.split(",");
                        addBook(tempArray[0], tempArray[1], tempArray[2], tempArray[3]);
                    }
                }
                read.close();
                successAlert("Books Added Successfully.");
                return;
            } catch (IOException e) {
                System.out.println("Error opening file");
                e.printStackTrace();
            }
        }
        errorAlert("File could not be opened.");
    }


    /**
     * Sends the book to be either checked in or out.
     * If more than one book is found, the multipleBooks() method is called.
     * If only one book is found, the singleBook() method is called.
     *
     * @param title  The title of the book.
     * @param either Either "in" or "out" as a String.
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    public void manageBook(String title, String either) throws IOException, SQLException {

        int count = 0;
        ArrayList<Book> tempBookList = new ArrayList<>();

        if (either.equals("in")) {
            count = searchCollection(title, "in");
            if (count == 1) {
                singleBook(title, "in", null);
            } else if (count > 1) {
                multipleBooks("in", tempBookList, null, false, title);
            } else {
                errorAlert("Book not found.");
            }
        } else if (either.equals("out")) {
            count = searchCollection(title, "out");
            if (count == 1) {
                singleBook(title, "out", LocalDate.now().plusMonths(1));
            } else if (count > 1) {
                multipleBooks("out", tempBookList, LocalDate.now().plusMonths(1), false, title);
            } else {
                errorAlert("Book not found.");
            }
        } else {
            errorAlert("Invalid operation type.");
        }
    }


    /**
     * If the searchCollection() method finds multiple books, this method is called.
     * It changes the availability and due date of the book according to whether it is being checked in or out.
     * If delete is true, it deletes the book instead of checking it in/out.
     *
     * @param either       Either "in" or "out" as a String.
     * @param tempBookList An ArrayList containing Book objects.
     * @param date         The LocalDate representing the due date for checking out a book (null for checking in).
     * @param delete       A Boolean indicating whether to delete the book.
     * @param searchTitle  The title of the book to be modified or deleted.
     * @throws IOException If an I/O error occurs.
     */
    public void multipleBooks(String either, ArrayList<Book> tempBookList, LocalDate date, Boolean delete, String searchTitle) throws IOException {
        try {

            // -- Setting up the stage -- //
            String query;
            Stage multipleBooksStage = new Stage();
            multipleBooksStage.initModality(Modality.APPLICATION_MODAL);

            if (delete){
                multipleBooksStage.setTitle("Select book to delete");
                query = "SELECT  * FROM books WHERE title = '" + searchTitle + "'";
            }else {
                multipleBooksStage.setTitle("Select book to check " + either);
                if (either.equals("in")){
                    query = "SELECT  * FROM books WHERE title = '" + searchTitle + "' AND available = false";
                }
                else {
                    query = "SELECT  * FROM books WHERE title = '" + searchTitle + "' AND available = true";
                }
            }

            Statement statement = dbConn.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxmlVisuals/multipleBooks.fxml")));
            Scene scene = new Scene(root);
            multipleBooksStage.setScene(scene);
            multipleBooksStage.setResizable(false);
            multipleBooksStage.getIcons().add(new Image("img/icon.png"));
            ListView<String> listView = (ListView<String>) scene.lookup("#multipleBooksListView");
            Button submitButton = (Button) scene.lookup("#multipleBooksButton");
            ObservableList<String> items = FXCollections.observableArrayList();


            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int barcode = resultSet.getInt("barcode");
                boolean availability = resultSet.getBoolean("available");
                java.util.Date dueDate = resultSet.getDate("due date");
                int volume = resultSet.getInt("volume");
                tempBookList.add((new Book(title,author,barcode,volume,availability,dueDate)));
            }

            for (Book book : tempBookList) {
                items.add(book.toString());
            }


            listView.setItems(items);


            // -- SUBMIT BUTTON ACTION LISTENER -- //
            submitButton.setOnAction(e -> {
                int index = listView.getSelectionModel().getSelectedIndex();
                if (index != -1) {
                    int barcodeNum = tempBookList.get(index).getBarcode();
                    try {
                        if (delete) {
                            // Delete the book from the database
                            try (PreparedStatement deleteStatement = dbConn.connection.prepareStatement("DELETE FROM books WHERE barcode = ?")) {
                                deleteStatement.setInt(1, barcodeNum);
                                deleteStatement.executeUpdate();
                            }
                        } else {
                            if (either.equals("in")) {
                                // Update the book as available in the database
                                try (PreparedStatement updateStatement = dbConn.connection.prepareStatement("UPDATE books SET available = true, `due date` = NULL WHERE barcode = ?")) {
                                    updateStatement.setInt(1, barcodeNum);
                                    updateStatement.executeUpdate();
                                }
                            } else {
                                // Update the book as checked out in the database
                                try (PreparedStatement updateStatement = dbConn.connection.prepareStatement("UPDATE books SET available = false, `due date` = ? WHERE barcode = ?")) {
                                    updateStatement.setDate(1, Date.valueOf(date));
                                    updateStatement.setInt(2, barcodeNum);
                                    updateStatement.executeUpdate();
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    multipleBooksStage.close();

                    if (delete){
                        try {
                            successAlert("Book successfully deleted.");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else {
                        try {
                            successAlert("Book successfully checked " + either + ".");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });

            // -- SCENE CLOSING ACTION LISTENER TO DISPLAY ERROR -- //
            multipleBooksStage.setOnCloseRequest(event -> {
                int index = listView.getSelectionModel().getSelectedIndex();
                if (index == -1) {
                    try {
                        errorAlert("Please select a book from the list.");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            multipleBooksStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            if (delete){
                errorAlert("Book could not be deleted.");
            }else {
                errorAlert("Book could not be checked " + either + ".");
            }
        }
    }


    /**
     * If the searchCollection method only finds one book, this method is called.
     * It changes the availability and due date of the book according to whether it is being checked in or out.
     *
     * @param title  The title of the book to be modified.
     * @param either Either "in" or "out" as a String.
     * @param date   The LocalDate representing the due date for checking out a book (null for checking in).
     * @throws IOException If an I/O error occurs.
     */
    public void singleBook(String title, String either, LocalDate date) throws IOException {
        try {
            String query;
            if (either.equals("in")) {
                query = "UPDATE books SET available = true, `due date` = NULL WHERE title = ?";
            } else {
                query = "UPDATE books SET available = false, `due date` = ? WHERE title = ?";
            }
            PreparedStatement statement = dbConn.connection.prepareStatement(query);
            if (!either.equals("in")) {
                statement.setDate(1, Date.valueOf(date));
                statement.setString(2, title);
            } else {
                statement.setString(1, title);
            }
            statement.executeUpdate();
            successAlert("Book successfully checked " + either + ".");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            errorAlert("Book could not be checked " + either + ".");
        }
    }


    /**
     * Queries the database to see if there are multiple books with the same name and availability status.
     * Returns the number of books that meet the criteria.
     *
     * @param title  The title of the book to be searched.
     * @param either Either "in" or "out" as a String.
     * @return The count of books that meet the search criteria.
     * @throws SQLException If a SQL error occurs.
     */
    public int searchCollection(String title, String either) throws SQLException {
        String sql;
        if (either.equals("in")) {
            sql = "SELECT COUNT(*) AS Matches FROM books WHERE title = ? AND available = false";
        } else if (either.equals("out")) {
            sql = "SELECT COUNT(*) AS Matches FROM books WHERE title = ? AND available = true";
        } else {
            throw new IllegalArgumentException("Invalid value for 'either' parameter. Please use 'in' or 'out'.");
        }

        PreparedStatement statement = dbConn.connection.prepareStatement(sql);
        statement.setString(1, title);
        ResultSet resultSet = statement.executeQuery();

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt("Matches");
        }
        return count;
    }


    /**
     * Calls the createAndShowAlert method to create a success alert using the message parameter.
     *
     * @param message The message to be displayed in the alert.
     * @throws IOException If an I/O error occurs.
     */
    public void successAlert(String message) throws IOException {
        createAndShowAlert("Success", "fxmlVisuals/success.fxml", message);
    }


    /**
     * Calls the createAndShowAlert method to create an error alert using the message parameter.
     *
     * @param message The message to be displayed in the alert.
     * @throws IOException If an I/O error occurs.
     */
    public void errorAlert(String message) throws IOException {
        createAndShowAlert("Error", "fxmlVisuals/error.fxml", message);
    }


    /**
     * Creates a new error or success alert depending on which method calls it.
     *
     * @param title   The title of the alert.
     * @param path    The path to the FXML file for the alert.
     * @param message The message to be displayed in the alert.
     * @throws IOException If an I/O error occurs.
     */
    private void createAndShowAlert(String title, String path, String message) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle(title);
        stage.getIcons().add(new Image("img/icon.png"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(root);

        if (title.equals("Error")){
            Label errorText = (Label) scene.lookup("#errorText");
            errorText.setText(message);
        }
        else{
            Label successText = (Label) scene.lookup("#successText");
            successText.setText(message);
        }

        stage.setScene(scene);
        stage.show();
    }
}