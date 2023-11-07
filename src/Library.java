/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * October 26, 2023.
 * Library.java
 * This class manages the book collection within the book management system. It takes care of
 * adding, removing, checkin in and checking out books.
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


public class Library {

    DBConnector dbConn = new DBConnector();

    public Library(){}


    ArrayList<Book> bookCollection = new ArrayList<>();


    /**
     * method: addBook()
     * parameters: String barcode, String title, String author
     * return: n/a
     * purpose: Adds a book to the collection.
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
     * method: removeBook(int)
     * parameters: int barcode
     * return: n/a
     * purpose: Removes a book from the collection using the book's barcode as reference.
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
     * method: removeBook(String)
     * parameters: String title
     * return: n/a
     * purpose: Removes books from the collection that have the inputted title, if there are multiple
     * books with the same title, prompts the user to select the specific book to be deleted.
     */
    public void removeBook(String title) throws IOException {
        Iterator<Book> iterator = bookCollection.iterator();
        ArrayList<Book> tempBookList = new ArrayList<>();
        int bookCount = 0;

        for (Book book : bookCollection) {
            if (book.getTitle().equals(title)) {
                tempBookList.add(book);
                bookCount += 1;
            }
        }

        if (bookCount == 1){
            while (iterator.hasNext()) {
                Book book = iterator.next();
                if (book.getTitle().equals(title)) {
                    iterator.remove();
                    successAlert("Book successfully deleted.");
                    return;
                }
            }
        }
        else if (bookCount > 1) {

            multipleBooks("in",true,tempBookList,null,true,title);

        }
        else {
            errorAlert("Book could not be found.");
        }

    }


    /**
     * method: openFile()
     * parameters: String filePath
     * return: n/a
     * purpose: Opens and reads the chosen file from the FileChooser.
     * Sends the book information to addBook() so that it can be added to the collection.
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


                while ((line = read.readLine()) != null) {
                    String[] tempArray = line.split(",");
                    addBook(tempArray[0], tempArray[1], tempArray[2], tempArray[3]);
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
     * method: checkIn()
     * parameters: String title
     * return: n/a
     * purpose: Checks a book back in after it has been checked out. Displays an "error" if book is already checked in.
     */
    public void checkIn (String title) throws IOException, SQLException {

        int count = 0;
        ArrayList<Book> tempBookList = new ArrayList<>();

        count = searchCollection(title,"in");

        if (count == 1){

            singleBook(title,"in", null);

        }
        else if (count > 1) {

            multipleBooks("in",true,tempBookList,null,false,title);

        }
        else {
            errorAlert("Book not found.");
        }

    }


    /**
     * method: checkOut()
     * parameters: String title
     * return: n/a
     * purpose: Checks an available book out. Displays an "error" if book can't be checked out.
     */
    public void checkOut (String title) throws IOException, SQLException {


        ArrayList<Book> tempBookList = new ArrayList<>();
        int count = 0;

        count = searchCollection(title,"out");

        if (count == 1) {

            singleBook(title,"out", LocalDate.now().plusMonths(1));

        }
        else if (count > 1) {
            multipleBooks("out",false,tempBookList,LocalDate.now().plusMonths(1),false, title);

        }
        else {
            errorAlert("Book not found.");
        }
    }


    /**
     * method: multipleBooks()
     * parameters: Scanner scan, String either, Boolean available, ArrayList<Book> tempBookList, LocalDate date, Boolean delete
     * return: n/a
     * purpose: If the searchCollection method finds multiple books, this method is called. It changes the availability
     * and due date of the book according to whether it is being checked in or out. If delete is true deletes book instead
     * of checking it in/out
     */
    public void multipleBooks(String either, Boolean available, ArrayList<Book> tempBookList, LocalDate date, Boolean delete, String searchTitle) throws IOException {
        try {

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
     * method: singleBook()
     * parameters: String title, String either, Boolean available, LocalDate date
     * return: n/a
     * purpose: If the searchCollection method only finds one book, this method is called. It changes the availability
     * and due date of the book according to whether it is being checked in or out.
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
     * method: searchCollection
     * parameters: String title, int count, ArrayList<Book> tempBookList, String either
     * return: int count
     * purpose: Searches to see if there are multiple books with the same name and availability status. Returns the
     * number of books that meet the criteria.
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
     * method: successAlert()
     * parameters: n/a
     * return: n/a
     * purpose: calls the createAndShowAlert method to create a Success Alert
     */
    public void successAlert(String message) throws IOException {
        createAndShowAlert("Success", "fxmlVisuals/success.fxml", message);
    }


    /**
     * method: errorAlert()
     * parameters: n/a
     * return: n/a
     * purpose: calls the createAndShowAlert method to create an Error Alert
     */
    public void errorAlert(String message) throws IOException {
        createAndShowAlert("Error", "fxmlVisuals/error.fxml", message);
    }


    /**
     * method: createAndShowAlert()
     * parameters: String title, String path
     * return: n/a
     * purpose: creates a new error or success alert
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