/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * August 31, 2023.
 * Library.java
 * This class manages the book collection within the book management system. It takes care of
 * adding, removing, and printing books in the collection.
 */

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
                System.out.println("Book successfully removed.");
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
                    System.out.println("Book successfully removed.");
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
     * method: printCollection()
     * parameters: n/a
     * return: n/a
     * purpose: Prints the whole collection of books to the console.
     */
    public void printCollection() {

        System.out.println();
        for (Book book : bookCollection) {
            System.out.println(book.toString());
        }

    }


    /**
     * method: openFile()
     * parameters: String filePath
     * return: n/a
     * purpose: Opens and reads the file from the specified file path.
     *          Sends the book information to addBook() so that it can be added to the collection.
     */
    public void openFile (String filePath) {
        try {
            BufferedReader read = new BufferedReader(new FileReader(filePath));
            String line;


            while ((line = read.readLine()) != null) {
                String[] tempArray = line.split(",");
                addBook(tempArray[0], tempArray[1], tempArray[2]);
            }

            read.close();

        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("File not found.");
        }
    }


    /**
     * method: checkIn()
     * parameters: String filePath
     * return: n/a
     * purpose: Checks a book back in after it has been checked out.
     */
    public void checkIn (String title, Scanner scan) {
        if (isAvailable(title)) {
            System.out.println("Book is already checked in");
        }
        else {
            System.out.println("Continue to check in here");
        }
    }


    /**
     * method: checkOut()
     * parameters: String filePath
     * return: n/a
     * purpose: Checks an available book out.
     */
    public void checkOut (String title, Scanner scan) {
        if (isAvailable(title)) {
            ArrayList<Book> tempBookList = new ArrayList<>();
            int count = 0;
            for (Book book : bookCollection){
                if (book.getTitle().equals(title) && book.getAvailability()){
                    tempBookList.add(book);
                    count += 1;
                }
            }

            if (count > 1) {
                System.out.println("Multiple books are available to be checked out under that title:");
                for (Book book : tempBookList) {
                    System.out.println(book.toString());
                }
                System.out.println();
                System.out.print("Enter the Barcode of the book you want to check out: ");
                int barcodeNum = scan.nextInt();
                for (Book book : bookCollection) {
                    if (book.getBarcode() == barcodeNum) {
                        book.setAvailability(false);
                    }
                }
                System.out.println("The book has been successfully checked out.");
            }
            else  {
                System.out.println("The following book:");
                for (Book book : bookCollection) {
                    if (book.getTitle().equals(title)) {
                        System.out.println(book.toString());
                        book.setAvailability(false);
                    }
                }
                System.out.println("Has been checked out.");
            }

        }
        else {
            System.out.println("Book is not available");
        }
    }


    public boolean isAvailable(String title){
        int availableCount = 0;
        for (Book book : bookCollection){
            if (book.getTitle().equals(title) && book.getAvailability()){
                availableCount += 1;
            }
        }

        if (availableCount > 0) {
            return true;
        }
        else{
            return false;
        }
    }
}