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
     * purpose: Removes all books from the collection that have the inputted title.
     */
    public void removeBook(String title){
        Iterator<Book> iterator = bookCollection.iterator();
        int booksRemoved = 0;

        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getTitle().equals(title)) {
                iterator.remove();
                booksRemoved += 1;
            }
        }

        if (booksRemoved > 0){
            System.out.println(booksRemoved + "Book(s) removed.");
        }
        else{
            System.out.println("Book(s) not found.");
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


}


