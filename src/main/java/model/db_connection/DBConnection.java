package model.db_connection;

import com.mysql.jdbc.Driver;
import constants.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection(){
        Connection c;

        try {
            DriverManager.registerDriver(new Driver());
            c = DriverManager.getConnection(DB.url,DB.username,DB.password);
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
