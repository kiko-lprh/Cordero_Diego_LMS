/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * August 31, 2023.
 * Main.java
 * This class serves as the main interface for the Library Management System. It presents a menu to the
 * user and handles user inputs.
 */

import java.util.Scanner;


public class Main {


    static Library bookstore = new Library();


    /**
     * method: main()
     * parameters: String[] args
     * return: n/a
     * purpose: Displays menu to user
     */
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String choice;

        do {

            System.out.println("\n(A)dd books from file.");
            System.out.println("Delete book by (B)arcode #.");
            System.out.println("Delete book by (T)itle.");
            System.out.println("Check book (i)n.");
            System.out.println("Check book (o)ut.");
            System.out.println("(P)rint book collection.");
            System.out.println("(Q)uit.\n");
            System.out.print("Choice: ");
            choice = scan.next();
            scan.nextLine();
            menuSwitch(choice, scan);


        } while (!choice.equalsIgnoreCase("q"));
    }


    /**
     * method: menuSwitch()
     * parameters: String choice, Scanner scan
     * return: n/a
     * purpose: Handles the menu displayed to the user
     */
    public static void menuSwitch (String choice, Scanner scan) {

        switch (choice.toLowerCase()) {
            case "a" -> bookstore.openFile(getPath(scan));
            case "b" -> bookstore.removeBook(getBarcode(scan));
            case "i" -> bookstore.checkIn(getTitle(scan), scan);
            case "o" -> bookstore.checkOut(getTitle(scan), scan);
            case "p" -> bookstore.printCollection();
            case "t" -> bookstore.removeBook(getTitle(scan), scan);
            default -> {
            }
        }
    }


    /**
     * method: getPath()
     * parameters: Scanner scan
     * return: string
     * purpose: Get the path of the file to be opened from the user.
     */
    public static String getPath(Scanner scan){


        System.out.print("Enter filepath: ");
        String path = scan.nextLine();
        System.out.println();

        return path;
    }


    /**
     * method: getBarcode()
     * parameters: Scanner scan
     * return: int
     * purpose: Get the barcode of the book that is going to be deleted from the user.
     */
    public static int getBarcode(Scanner scan){
        System.out.print("Enter the Book's Barcode #: ");
        int barcode = scan.nextInt();
        System.out.println();
        return barcode;
    }


    /**
     * method: getTitle()
     * parameters: Scanner scan
     * return: String
     * purpose: Get the title a book from the user.
     */
    public static String getTitle(Scanner scan){
        System.out.print("Enter the Book's Title: ");
        String title = scan.nextLine();
        System.out.println();
        return title;
    }

}