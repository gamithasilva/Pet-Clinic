package lk.ijse.petclinic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final static String DB_URL = "jdbc:mysql://localhost:3307/petclinic";
    private final static String DB_USERNAME = "root";
    private final static String DB_PASSWORD = "1234";

    private static Connection connection;


    private DBConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        connection=conn;
    }

    public static DBConnection getInstance() throws SQLException {
        DBConnection dbc = new DBConnection();
        return dbc;
    }

    public Connection getConnection() {
        return connection;
    }

}


