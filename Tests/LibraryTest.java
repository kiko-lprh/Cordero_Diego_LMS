import java.time.LocalDate;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    Library testLibrary = new Library();

    @org.junit.jupiter.api.Test
    void addBookSingle() {
        // Test adding a book to the "database".
        testLibrary.addBook("1101","Book 1", "Diego Cordero");

        // Check if the book was added to the library.
        assertEquals(1, testLibrary.bookCollection.size());
    }


    @org.junit.jupiter.api.Test
    void addBookMultiple() {
        // Test adding multiple books to the "database".
        testLibrary.addBook("1101","Book 1", "Diego Cordero");
        testLibrary.addBook("1102","Book 1", "Diego Cordero");
        testLibrary.addBook("1103","Book 1", "Diego Cordero");
        testLibrary.addBook("1104","Book 1", "Diego Cordero");
        testLibrary.addBook("1105","Book 1", "Diego Cordero");
        testLibrary.addBook("1106","Book 1", "Diego Cordero");
        testLibrary.addBook("1107","Book 1", "Diego Cordero");
        testLibrary.addBook("1108","Book 1", "Diego Cordero");
        testLibrary.addBook("1109","Book 1", "Diego Cordero");
        testLibrary.addBook("1110","Book 1", "Diego Cordero");
        testLibrary.addBook("1111","Book 1", "Diego Cordero");
        testLibrary.addBook("1112","Book 1", "Diego Cordero");
        testLibrary.addBook("1113","Book 1", "Diego Cordero");
        testLibrary.addBook("1114","Book 1", "Diego Cordero");
        testLibrary.addBook("1115","Book 1", "Diego Cordero");
        testLibrary.addBook("1116","Book 1", "Diego Cordero");
        testLibrary.addBook("1117","Book 1", "Diego Cordero");
        testLibrary.addBook("1118","Book 1", "Diego Cordero");
        testLibrary.addBook("1119","Book 1", "Diego Cordero");
        testLibrary.addBook("1120","Book 1", "Diego Cordero");

        // Check if the book were added to the library.
        assertEquals(20, testLibrary.bookCollection.size());
    }


    @org.junit.jupiter.api.Test
    void removeBookByBarcode() {

        //Add book to the library collection
        testLibrary.addBook("1101","Book 1", "Diego Cordero");

        //Remove the book using its barcode
        testLibrary.removeBook(1101);

        // Check if the book is removed from the library
        assertEquals(0,testLibrary.bookCollection.size());

    }


    @org.junit.jupiter.api.Test
    void removeBookByTitleSingle() {

        Scanner scan = new Scanner(System.in);

        //Add book to the library collection
        testLibrary.addBook("1101","Book 1", "Diego Cordero");

        //Remove the book using its title
        testLibrary.removeBook("Book 1", scan);

        // Check if the book is removed from the library
        assertEquals(0, testLibrary.bookCollection.size());
    }


    @org.junit.jupiter.api.Test
    void removeBookByTitleMultiple() {

        Scanner scan = new Scanner("1104\n");

        //Add books to the library collection
        testLibrary.addBook("1101","Book 1", "Diego Cordero");
        testLibrary.addBook("1102","Book 1", "Diego Cordero");
        testLibrary.addBook("1103","Book 1", "Diego Cordero");
        testLibrary.addBook("1104","Book 1", "Diego Cordero");
        testLibrary.addBook("1105","Book 1", "Diego Cordero");
        testLibrary.addBook("1106","Book 1", "Diego Cordero");

        // Check that the book collection has the correct amount of books
        assertEquals(6, testLibrary.bookCollection.size());

        //Remove the book using its title
        testLibrary.removeBook("Book 1", scan);

        // Check if the book was removed from the library
        assertEquals(5, testLibrary.bookCollection.size());


    }


    @org.junit.jupiter.api.Test
    void checkInSingle() {

        Scanner scan = new Scanner(System.in);

        // Add book to the library collection
        testLibrary.addBook("1101","Book 1", "Diego Cordero");

        // Check book out and then back in
        testLibrary.checkOut("Book 1", scan);
        testLibrary.checkIn("Book 1", scan);

        // Check if book's due date is correctly set
        assertNull(testLibrary.bookCollection.get(0).getDueDate());

        // Check if book is available to be checked out
        assertEquals(true, testLibrary.bookCollection.get(0).getAvailability());

    }


    @org.junit.jupiter.api.Test
    void checkInMultiple() {

        LocalDate expected = LocalDate.now().plusMonths(1);

        // Add book to the library collection
        testLibrary.addBook("1101","Book 1", "Diego Cordero");
        testLibrary.addBook("1102","Book 1", "Diego Cordero");
        testLibrary.addBook("1103","Book 3", "Diego Cordero");
        testLibrary.addBook("1104","Book 1", "Diego Cordero");
        testLibrary.addBook("1105","Book 7", "Diego Cordero");
        testLibrary.addBook("1106","Book 8", "Diego Cordero");

        // Check every book out
        testLibrary.checkOut("Book 1", new Scanner("1101\n"));
        testLibrary.checkOut("Book 3", new Scanner(""));
        testLibrary.checkOut("Book 1", new Scanner("1104"));
        testLibrary.checkOut("Book 1", new Scanner(""));
        testLibrary.checkOut("Book 7", new Scanner(""));
        testLibrary.checkOut("Book 8", new Scanner(""));

        // Only check some of them back in
        testLibrary.checkIn("Book 1", new Scanner("1101\n"));
        testLibrary.checkIn("Book 1", new Scanner("1102\n"));
        testLibrary.checkIn("Book 1", new Scanner(""));
        testLibrary.checkIn("Book 3", new Scanner(""));

        /* Check if each book's due date is correctly set. Only the ones that were checked back in should be set
        to NULL. The rest should have the date set to one month from now. */
        assertNull(testLibrary.bookCollection.get(0).getDueDate());
        assertNull(testLibrary.bookCollection.get(1).getDueDate());
        assertNull(testLibrary.bookCollection.get(2).getDueDate());
        assertNull(testLibrary.bookCollection.get(3).getDueDate());
        assertEquals(expected,testLibrary.bookCollection.get(4).getDueDate());
        assertEquals(expected,testLibrary.bookCollection.get(4).getDueDate());

        /* Check if every book is available to be checked out. Only the books that were checked back in should
        be set to true */
        assertEquals(true, testLibrary.bookCollection.get(0).getAvailability());
        assertEquals(true, testLibrary.bookCollection.get(1).getAvailability());
        assertEquals(true, testLibrary.bookCollection.get(2).getAvailability());
        assertEquals(true, testLibrary.bookCollection.get(3).getAvailability());
        assertEquals(false, testLibrary.bookCollection.get(4).getAvailability());
        assertEquals(false, testLibrary.bookCollection.get(5).getAvailability());

    }


    @org.junit.jupiter.api.Test
    void checkOutSingle() {

        LocalDate expected = LocalDate.now().plusMonths(1);
        Scanner scan = new Scanner(System.in);

        // Add book to the library collection
        testLibrary.addBook("1105","Book 7", "Diego Cordero");

        // Check book out
        testLibrary.checkOut("Book 7", scan);

        // Check if book's due date is correctly set
        assertEquals(expected,testLibrary.bookCollection.get(0).getDueDate());

        // Check if book is not available to be checked out
        assertEquals(false, testLibrary.bookCollection.get(0).getAvailability());

    }


    @org.junit.jupiter.api.Test
    void checkOutMultiple() {

        LocalDate expected = LocalDate.now().plusMonths(1);

        // Add books to the library collection
        testLibrary.addBook("1105","Book 7", "Diego Cordero");
        testLibrary.addBook("1106","Book 7", "Diego Cordero");
        testLibrary.addBook("1107","Book 7", "Diego Cordero");
        testLibrary.addBook("1108","Book 9", "Diego Cordero");
        testLibrary.addBook("1109","Book 10", "Diego Cordero");
        testLibrary.addBook("1110","Book 11", "Diego Cordero");
        testLibrary.addBook("1111","Book 11", "Diego Cordero");
        testLibrary.addBook("1112","Book 22", "Diego Cordero");
        testLibrary.addBook("1113","Book 22", "Diego Cordero");

        // Only check some books out
        testLibrary.checkOut("Book 7", new Scanner("1106\n"));
        testLibrary.checkOut("Book 10", new Scanner(""));
        testLibrary.checkOut("Book 11", new Scanner("1111\n"));
        testLibrary.checkOut("Book 22", new Scanner("1113\n"));
        testLibrary.checkOut("Book 22", new Scanner(""));

        // Check if each book's due date is correctly set. The ones that weren't checked out should be null.
        assertNull(testLibrary.bookCollection.get(0).getDueDate());
        assertEquals(expected,testLibrary.bookCollection.get(1).getDueDate());
        assertNull(testLibrary.bookCollection.get(2).getDueDate());
        assertNull(testLibrary.bookCollection.get(3).getDueDate());
        assertEquals(expected,testLibrary.bookCollection.get(4).getDueDate());
        assertNull(testLibrary.bookCollection.get(5).getDueDate());
        assertEquals(expected,testLibrary.bookCollection.get(6).getDueDate());
        assertEquals(expected,testLibrary.bookCollection.get(7).getDueDate());
        assertEquals(expected,testLibrary.bookCollection.get(8).getDueDate());

        // Check if each book's availability is correct. The ones that weren't checked should be set to true.
        assertEquals(true, testLibrary.bookCollection.get(0).getAvailability());
        assertEquals(false, testLibrary.bookCollection.get(1).getAvailability());
        assertEquals(true, testLibrary.bookCollection.get(2).getAvailability());
        assertEquals(true, testLibrary.bookCollection.get(3).getAvailability());
        assertEquals(false, testLibrary.bookCollection.get(4).getAvailability());
        assertEquals(true, testLibrary.bookCollection.get(5).getAvailability());
        assertEquals(false, testLibrary.bookCollection.get(6).getAvailability());
        assertEquals(false, testLibrary.bookCollection.get(7).getAvailability());
        assertEquals(false, testLibrary.bookCollection.get(8).getAvailability());


    }
}