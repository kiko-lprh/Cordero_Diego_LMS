/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * August 31, 2023.
 * Library.java
 * This class manages the book collection within the book management system. It takes care of
 * adding, removing, and printing books in the collection.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;
import java.util.Iterator;
import java.util.Scanner;


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
    public void removeBook(int barcode){
        Iterator<Book> iterator = bookCollection.iterator();

        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getBarcode() == barcode) {
                iterator.remove();
                System.out.println("Book successfully removed.\n");
                //printCollection();
                return;
            }
        }
        System.out.println("Book not found.");
    }


    /**
     * method: removeBook(String)
     * parameters: String title
     * return: n/a
     * purpose: Removes books from the collection that have the inputted title, if there are multiple
     * books with the same title, prompts the user to enter the book's barcode number to delete it.
     */
    public void removeBook(String title, Scanner scan){
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
                    System.out.println("Book successfully removed.\n");
                    //printCollection();
                    return;
                }
            }
        }
        else if (bookCount > 1) {
            System.out.println("Books found:\n");

            for (Book book : tempBookList) {
                System.out.println(book.toString());
            }

            System.out.println();
            System.out.print("Enter the Barcode of the book you want to delete: ");
            removeBook(scan.nextInt());
            System.out.println();
        }
        else {
            System.out.println("Book not found");
        }

    }


    /**
     * method: openFile()
     * parameters: String filePath
     * return: n/a
     * purpose: Opens and reads the file from the specified file path.
     * Sends the book information to addBook() so that it can be added to the collection.
     */
    public void openFile (String filePath) {
        try {
            BufferedReader read = new BufferedReader(new FileReader(filePath));
            String line;


            while ((line = read.readLine()) != null) {
                String[] tempArray = line.split(",");
                addBook(tempArray[0], tempArray[1], tempArray[2]);
            }
            System.out.println("File opened successfully.");
            read.close();

            //printCollection();

            System.out.println("\nBooks added.");

        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("File not found.");
        }
    }


    /**
     * method: checkIn()
     * parameters: String title, Scanner scan
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

            multipleBooks("in",true,tempBookList,null);

        }
        else {
            System.out.println("Error: Book could not be checked in.");
        }

    }


    /**
     * method: checkOut()
     * parameters: String title, Scanner scan
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
            multipleBooks("out",false,tempBookList,LocalDate.now().plusMonths(1));

        }
        else {
            System.out.println("Error: Book could not be checked out.");
        }
    }


    /**
     * method: multipleBooks()
     * parameters: Scanner scan, String either, Boolean available, ArrayList<Book> tempBookList, LocalDate date
     * return: n/a
     * purpose: If the searchCollection method finds multiple books, this method is called. It changes the availability
     * and due date of the book according to whether it is being checked in or out.
     */
    public void multipleBooks(String either, Boolean available, ArrayList<Book> tempBookList, LocalDate date) {

        Stage multipleBooksStage = new Stage();
        multipleBooksStage.initModality(Modality.APPLICATION_MODAL);
        multipleBooksStage.setTitle("Select Book");

        ObservableList<String> items = FXCollections.observableArrayList();
        for (Book book : tempBookList) {
            items.add(book.toString());
        }
        ListView<String> listView = new ListView<>(items);

        Label label = new Label("Select a book from the list:");
        TextField barcode = new TextField();
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index != -1) {
                int barcodeNum = tempBookList.get(index).getBarcode();
                for (Book book : bookCollection) {
                    if (book.getBarcode() == barcodeNum) {
                        book.setAvailability(available);
                        book.setDueDate(date);
                    }
                }
                System.out.println("The book has been successfully checked " + either + ".\n");
                multipleBooksStage.close();
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, listView, barcode, submitButton);

        Scene scene = new Scene(vBox, 400, 400);
        multipleBooksStage.setScene(scene);
        multipleBooksStage.setResizable(false);
        multipleBooksStage.getIcons().add(new Image("img/icon.png"));
        multipleBooksStage.showAndWait();
    }



    /**
     * method: singleBook()
     * parameters: String title, String either, Boolean available, LocalDate date
     * return: n/a
     * purpose: If the searchCollection method only finds one book, this method is called. It changes the availability
     * and due date of the book according to whether it is being checked in or out.
     */
    public void singleBook(String title, String either, Boolean available, LocalDate date){
        for (Book book : bookCollection) {
            if (book.getTitle().equals(title)) {
                if ((either.equals("in") && !book.getAvailability()) || (either.equals("out") && book.getAvailability())) {
                    book.setAvailability(available);
                    book.setDueDate(date);
                }
            }
        }
        System.out.println("Has been checked " + either + ".\n");
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

}