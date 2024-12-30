package model.db;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static String url = "jdbc:mysql://localhost:3306/clinic_cure";
    public static String username = "root";
    public static String password = "123123a@";

    public static Connection getConnection(){
        Connection c;

        try {
            DriverManager.registerDriver(new Driver());

            c = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return c;
    }

    public static void closeConnection(Connection c){
        try {
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
