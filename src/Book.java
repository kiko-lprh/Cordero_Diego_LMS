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

    @Override
    public String toString() {
        return "ID: " + id + ", " + "Title: " + title + ", " + "Author: " + author + ".";
    }
}
