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
    int id;

    public Book (String title, String author, int id){
        this.title = title;
        this.author = author;
        this.id = id;
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


    public void setID(int id) {
        this.id = id;
    }


    public int getID() {
        return id;
    }


    /**
     * method: toString()
     * parameters: n/a
     * return: String
     * purpose: Overrides the toString method to accurately format the book object as a printable String
     */
    @Override
    public String toString() {
        return "ID: " + id + ", " + "Title: " + title + ", " + "Author: " + author + ".";
    }
}