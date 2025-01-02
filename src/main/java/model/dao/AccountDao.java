package model.dao;

import model.base.Account;
import model.db_connection.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AccountDao implements Dao<Account>{
    public static AccountDao getInstance(){
        return new AccountDao();
    }

    @Override
    public int insert(Account value) {
        return 0;
    }

    @Override
    public int update(Account value) {
        return 0;
    }

    @Override
    public int delete(Account value) {
        return 0;
    }

    @Override
    public Account selectById(String id) {
        Account acc = null;
        Connection c = DBConnection.getConnection();

        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        DBConnection.closeConnection(c);
        return new Account();
    }

    @Override
    public ArrayList<Account> selectAll() {
        return null;
    }

    @Override
    public ArrayList<Account> selectByCondition(String condition) {
        return null;
    }
}
