package model.dao;

import constants.AccountType;
import model.base.Account;
import model.db_connection.DBConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class AccountDao implements Dao<Account> {
    public static AccountDao getInstance() {
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
            String sql = "select * from accounts where accountId = ?;";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String accountId = rs.getString("accountId");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String accType = rs.getString("accountType");
                Icon avatar = rs.getBytes("avatar") == null ? null : new ImageIcon(rs.getBytes("avatar"));
                acc = new Account(accountId, username, password, AccountType.valueOf(accType), avatar);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        DBConnection.closeConnection(c);
        return acc;
    }

    @Override
    public ArrayList<Account> selectAll() {
        return null;
    }

    @Override
    public ArrayList<Account> selectByCondition(String condition) {
        return null;
    }

    public Account selectForLogin(String username, String password) {
        Account acc = null;
        Connection c = DBConnection.getConnection();

        try {
            String sql = "select * from accounts where username = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String accountId = rs.getString("accountId");
//                String username = rs.getString("username");
//                String password = rs.getString("password");
                String accType = rs.getString("accountType");
                Icon avatar = rs.getBytes("avatar") == null ? null : new ImageIcon(rs.getBytes("avatar"));
                if (Objects.equals(rs.getString("password"), password)) {
                    acc = new Account(accountId, username, password, AccountType.valueOf(accType), avatar);
                } else {
                    acc = new Account();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return acc;
    }
}
