import java.util.Scanner;


public class Main {


    static Library bookstore = new Library();


    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String choice;

        do {

            System.out.println("\n(A)dd books from file.");
            System.out.println("(D)elete book.");
            System.out.println("(P)rint book collection.");
            System.out.println("(Q)uit.\n");
            System.out.print("Choice: ");
            choice = scan.next();
            scan.nextLine();
            menuSwitch(choice, scan);


        } while (!choice.equalsIgnoreCase("q"));
    }


    public static void menuSwitch (String choice, Scanner scan) {

        switch (choice) {
            case "a", "A" -> bookstore.openFile(getPath(scan));
            case "d", "D" -> bookstore.removeBook(getID(scan));
            case "p", "P" -> bookstore.printCollection();
            default -> {
            }
        }
    }


    public static String getPath(Scanner scan){


        System.out.print("Enter desired filepath: ");
        String path = scan.nextLine();
        System.out.println();

        return path;
    }

    public static int getID(Scanner scan){
        System.out.print("Enter the Book's ID: ");
        int ID = scan.nextInt();
        System.out.println();
        return ID;
    }

}


