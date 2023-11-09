/**
 * Diego Cordero
 * CEN 3024 - Software Development 1
 * November 9, 2023.
 * DBConnector.java
 * This class handles the connection between the database and the program.
 */

import java.sql.*;

public class DBConnector {

    private final String url = "jdbc:mysql://localhost:3306/library_main";
    private final String username = "root";
    private final String password = "";
    Connection connection;

    public DBConnector() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}