import java.sql.*;

public class DBConnector {

    private final String url = "jdbc:mysql://localhost:3306/library_main";
    private final String username = "root";
    private final String password = "";
    Connection connection;

    public DBConnector() {
        try {

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

}