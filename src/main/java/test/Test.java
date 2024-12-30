package test;

import model.db.DBConnection;

import java.sql.Connection;

public class Test {
    public static void main(String[] args) {
        Connection c = DBConnection.getConnection();
        System.out.println(c);
    }
}
