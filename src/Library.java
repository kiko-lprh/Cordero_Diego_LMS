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

import java.time.LocalDate;
import java.util.*;
import java.io.*;


public class Library {


    public Library(){

    }


    ArrayList<Book> bookCollection = new ArrayList<>();


    /**
     * method: addBook()
     * parameters: String barcode, String title, String author
     * return: n/a
     * purpose: Adds a book to the collection.
     */
    public void addBook(String barcode, String title, String author) {
        bookCollection.add(new Book(title, author, Integer.parseInt(barcode)));
    }


    /**
     * method: removeBook(int)
     * parameters: int barcode
     * return: n/a
     * purpose: Removes a book from the collection using the book's barcode as reference.
     */
    public void removeBook(int barcode) throws IOException {
        Iterator<Book> iterator = bookCollection.iterator();

        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getBarcode() == barcode) {
                iterator.remove();
                successAlert();
                return;
            }
        }
        errorAlert();
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
                    successAlert();
                    return;
                }
            }
        }
        else if (bookCount > 1) {

            multipleBooks("in",true,tempBookList,null,true);

        }
        else {
            errorAlert();
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
        bookCollection.clear();
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
                    addBook(tempArray[0], tempArray[1], tempArray[2]);
                }
                read.close();
                successAlert();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        errorAlert();
    }


    /**
     * method: checkIn()
     * parameters: String title
     * return: n/a
     * purpose: Checks a book back in after it has been checked out. Displays an "error" if book is already checked in.
     */
    public void checkIn (String title) throws IOException {

        int count = 0;
        ArrayList<Book> tempBookList = new ArrayList<>();

        count = searchCollection(title,count,tempBookList,"in");

        if (count == 1){

            singleBook(title,"in",true,null);

        }
        else if (count > 1) {

            multipleBooks("in",true,tempBookList,null,false);

        }
        else {
            errorAlert();
        }

    }


    /**
     * method: checkOut()
     * parameters: String title
     * return: n/a
     * purpose: Checks an available book out. Displays an "error" if book can't be checked out.
     */
    public void checkOut (String title) throws IOException {


        ArrayList<Book> tempBookList = new ArrayList<>();
        int count = 0;

        count = searchCollection(title,count,tempBookList,"out");

        if (count == 1) {

            singleBook(title,"out",false,LocalDate.now().plusMonths(1));

        }
        else if (count > 1) {
            multipleBooks("out",false,tempBookList,LocalDate.now().plusMonths(1),false);

        }
        else {
            errorAlert();
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
    public void multipleBooks(String either, Boolean available, ArrayList<Book> tempBookList, LocalDate date, Boolean delete) throws IOException {
        try {
            Stage multipleBooksStage = new Stage();
            multipleBooksStage.initModality(Modality.APPLICATION_MODAL);

            if (delete){
                multipleBooksStage.setTitle("Select book to delete");
            }else {
                multipleBooksStage.setTitle("Select book to check " + either);
            }


            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxmlVisuals/multipleBooks.fxml")));
            Scene scene = new Scene(root);

            multipleBooksStage.setScene(scene);
            multipleBooksStage.setResizable(false);
            multipleBooksStage.getIcons().add(new Image("img/icon.png"));

            ListView<String> listView = (ListView<String>) scene.lookup("#multipleBooksListView");
            Button submitButton = (Button) scene.lookup("#multipleBooksButton");

            ObservableList<String> items = FXCollections.observableArrayList();

            for (Book book : tempBookList) {
                items.add(book.toString());
            }

            listView.setItems(items);

            submitButton.setOnAction(e -> {
                int index = listView.getSelectionModel().getSelectedIndex();
                if (index != -1) {
                    int barcodeNum = tempBookList.get(index).getBarcode();
                    ArrayList<Book> bookToRemove = new ArrayList<>();
                    for (Book book : bookCollection) {
                        if (book.getBarcode() == barcodeNum) {
                            if (delete) {
                                bookToRemove.add(book);
                            } else {
                                book.setAvailability(available);
                                book.setDueDate(date);
                            }
                        }
                    }
                    bookCollection.removeAll(bookToRemove);
                    multipleBooksStage.close();
                }
            });
            multipleBooksStage.showAndWait();
            successAlert();
        } catch (Exception e) {
            e.printStackTrace();
            errorAlert();
        }
    }


    /**
     * method: singleBook()
     * parameters: String title, String either, Boolean available, LocalDate date
     * return: n/a
     * purpose: If the searchCollection method only finds one book, this method is called. It changes the availability
     * and due date of the book according to whether it is being checked in or out.
     */
    public void singleBook(String title, String either, Boolean available, LocalDate date) throws IOException {
        for (Book book : bookCollection) {
            if (book.getTitle().equals(title)) {
                if ((either.equals("in") && !book.getAvailability()) || (either.equals("out") && book.getAvailability())) {
                    book.setAvailability(available);
                    book.setDueDate(date);
                }
            }
        }
        successAlert();
    }


    /**
     * method: searchCollection
     * parameters: String title, int count, ArrayList<Book> tempBookList, String either
     * return: int count
     * purpose: Searches to see if there are multiple books with the same name and availability status. Returns the
     * number of books that meet the criteria.
     */
    public int searchCollection(String title, int count, ArrayList<Book> tempBookList, String either){
        for (Book book : bookCollection){
            if (book.getTitle().equals(title)){
                if ((either.equals("in") && !book.getAvailability()) || (either.equals("out") && book.getAvailability())) {
                    tempBookList.add(book);
                    count += 1;
                }
            }
        }
        return count;
    }


    /**
     * method: successAlert()
     * parameters: n/a
     * return: n/a
     * purpose: calls the createAndShowAlert method to create a Success Alert
     */
    public void successAlert() throws IOException {
        createAndShowAlert("Success", "fxmlVisuals/success.fxml");
    }


    /**
     * method: errorAlert()
     * parameters: n/a
     * return: n/a
     * purpose: calls the createAndShowAlert method to create an Error Alert
     */
    public void errorAlert() throws IOException {
        createAndShowAlert("Error", "fxmlVisuals/error.fxml");
    }


    /**
     * method: createAndShowAlert()
     * parameters: String title, String path
     * return: n/a
     * purpose: creates a new error or success alert
     */
    private void createAndShowAlert(String title, String path) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle(title);
        stage.getIcons().add(new Image("img/icon.png"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}