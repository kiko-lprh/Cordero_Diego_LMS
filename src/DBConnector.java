/**
 * DBConnector.java
 * This class handles the connection between the database and the program.
 *
 * @author Diego Cordero
 * @version 1.0 Final
 * @date November 25, 2023.
 * @course CEN 3024 - Software Development 1
 */

import java.sql.*;

/**
 * Connects the LMS to the database.
 */
public class DBConnector {

    // Connection URL, username, and password
    private final String url = "jdbc:mysql://localhost:3306/library_main";
    private final String username = "root";
    private final String password = "";
    Connection connection;

    /**
     * Constructs a new DBConnector and establishes a connection to the database.
     */
    public DBConnector() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}