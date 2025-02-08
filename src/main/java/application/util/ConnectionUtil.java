package application.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The ConnectionUtil class will be utilized to create an active connection to our database. This class utilizes the
 * singleton design pattern. We will be utilizing an in-memory called h2database. In-memory means that the database
 * is dissolved when the program ends - it is only for use in testing. Do not change anything in this class.
 */
public class ConnectionUtil {
    
    // The URL represents the connection string to the database. Since we are using an in-memory database, 
    // we specify a file location to persist the data in the "./h2/db" directory.
    private static String url = "jdbc:h2:./h2/db";
    private static String username = "sa";
    private static String password = "sa";

    private static Connection connection = null;

    /**
     * @return active connection to the database
     */
    public static Connection getConnection(){
        if(connection == null){
            try {
                System.out.println("Creating new database connection...");
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database connection established.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error connecting to database: " + e.getMessage());
            }
        }

        return connection;
    }
}