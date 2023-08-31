import java.util.ArrayList;
import java.io.*;
import java.util.Iterator;


public class Library {

    public Library(){

    }


    ArrayList<Book> bookCollection = new ArrayList<>();


    public void addBook(String ID,String title, String author) {
        bookCollection.add(new Book(title, author, Integer.parseInt(ID)));
    }


    public void removeBook(int ID){
        Iterator<Book> iterator = bookCollection.iterator();

        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getID() == ID) {
                iterator.remove();
                System.out.println("Book successfully removed.");
                return;
            }
        }
        System.out.println("Book not found.");
    }


    public void printCollection() {

        System.out.println();
        for (Book book : bookCollection) {
            System.out.println(book.toString());
        }

    }

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


