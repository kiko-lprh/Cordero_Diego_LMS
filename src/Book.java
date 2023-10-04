/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * August 31, 2023.
 * Book.java
 * This class is the blueprint for a book in the LMS. Each instance of this class represents a
 * specific book.
 */

public class Book {

    String title;
    String author;
    int barcode;
    String dueDate; // Null by default
    Boolean borrowStatus; // if true book is checked out


    public Book (String title, String author, int barcode){
        this.title = title;
        this.author = author;
        this.barcode = barcode;
        dueDate = null;
        borrowStatus = false;
    }


    public void setAuthor(String author) {
        this.author = author;
    }


    public String getAuthor() {
        return author;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }


    public int getBarcode() {
        return barcode;
    }


    public void setDueDate(String dueDate){
        this.dueDate = dueDate;
    }


    public String getDueDate() {
        return dueDate;
    }


    public void setBorrowStatus(Boolean borrowStatus) {
        this.borrowStatus = borrowStatus;
    }


    public Boolean getBorrowStatus() {
        return borrowStatus;
    }


    /**
     * method: toString()
     * parameters: n/a
     * return: String
     * purpose: Overrides the toString method to accurately format the book object as a printable String
     */
    @Override
    public String toString() {
        return "Barcode #: " + barcode + ", " + "Title: " + title + ", " + "Author: " + author + ", "
                + "Due Date: " +  dueDate + ", " + "Borrow status: " + borrowStatus;
    }
}