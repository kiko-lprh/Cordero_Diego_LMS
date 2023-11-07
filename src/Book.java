/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * October 26, 2023.
 * Book.java
 * This class is the blueprint for a book in the LMS. Each instance of this class represents a
 * specific book.
 */

import java.time.LocalDate;
import java.util.Date;

public class Book {

    String title;
    String author;
    int barcode;
    String dueDate; // Null by default
    Boolean availability; // if true book is checked in
    int volume;


    public Book (String title, String author, int barcode, int volume, boolean availability, Date dueDate){
        this.title = title;
        this.author = author;
        this.barcode = barcode;
        this.dueDate = String.valueOf(dueDate);
        this.availability = availability;
        this.volume = volume;
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


    public void setDueDate(LocalDate dueDate){
        this.dueDate = String.valueOf(dueDate);
    }


    public String getDueDate() {
        return dueDate;
    }


    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }


    public Boolean getAvailability() {
        return availability;
    }


    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }


    /**
     * method: toString()
     * parameters: n/a
     * return: String
     * purpose: Overrides the toString method to accurately format the book object as a printable String
     */
    @Override
    public String toString() {
        return "Barcode #: " + barcode + ", " + "Title: " + title + ", " + "Volume: " + volume + ", " + "Author: " + author + ", "
                + "Due Date: " +  dueDate + ", " + "Available: " + availability;
    }
}