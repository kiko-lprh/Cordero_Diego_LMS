import java.util.ArrayList;
import java.io.*;


public class Library {

    public Library(){

    }


    ArrayList<Book> bookCollection = new ArrayList<Book>();


    public void addBook(String ID,String title, String author) {
        bookCollection.add(new Book(title, author, Integer.parseInt(ID)));
    }


    public void removeBook(int ID){

    }


    public void printCollection() {

        System.out.println();
        for (int i = 0; i < bookCollection.size(); i++){
            System.out.println(bookCollection.get(i).toString());
        }

    }

    public void openFile (String filePath) { //Change to STRING later
        try {
            BufferedReader read = new BufferedReader(new FileReader(filePath));
            String line;


            while ((line = read.readLine()) != null) {
                String tempArray[] = line.split(",");
                addBook(tempArray[0], tempArray[1], tempArray[2]);
            }


            read.close();


        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("File not found.");
        }
    }


}


