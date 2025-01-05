package model.dao;

import util.Utils;
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
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            PreparedStatement stmt;
            if (value.getAvatar() != null) {
                String sql = "insert into accounts (accountId, username, password, accountType, avatar) values (?, ?, ?, ?, ?);";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, value.getAccountId());
                stmt.setString(2, value.getUsername());
                stmt.setString(3, value.getPassword());
                stmt.setString(4, value.getAccountType().toString());
                stmt.setBytes(5, Utils.iconToBytes(value.getAvatar()));
            } else {
                String sql = "insert into accounts (accountId, username, password, accountType) values (?, ?, ?, ?);";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, value.getAccountId());
                stmt.setString(2, value.getUsername());
                stmt.setString(3, value.getPassword());
                stmt.setString(4, value.getAccountType().toString());
            }
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int update(Account value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            PreparedStatement stmt;
            if (value.getAvatar() != null) {
                String sql = "UPDATE accounts SET username = ?, password = ?, accountType = ?, avatar = ? WHERE accountId = ?;";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, value.getUsername());
                stmt.setString(2, value.getPassword());
                stmt.setString(3, value.getAccountType().toString());
                stmt.setBytes(4, Utils.iconToBytes(value.getAvatar()));
                stmt.setString(5, value.getAccountId());
            } else {
                String sql = "UPDATE accounts SET username = ?, password = ?, accountType = ? WHERE accountId = ?;";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, value.getUsername());
                stmt.setString(2, value.getPassword());
                stmt.setString(3, value.getAccountType().toString());
                stmt.setString(4, value.getAccountId());
            }
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int delete(Account value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "DELETE FROM accounts WHERE accountId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getAccountId());
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public Account selectById(String id) {
        Account acc = null;
        Connection c = DBConnection.getConnection();

        try {
            String sql = "select * from accounts where accountId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String accountId = rs.getString("accountId");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String accType = rs.getString("accountType");
                Icon avatar = rs.getBytes("avatar") == null ? new ImageIcon(getClass().getResource("/icon_login/user.png")) : new ImageIcon(rs.getBytes("avatar"));
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
        ArrayList<Account> accounts = new ArrayList<>();
        Connection c = DBConnection.getConnection();

        try {
            String sql = "select * from accounts;";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Account account = null;
                String accountId = rs.getString("accountId");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String accType = rs.getString("accountType");
                Icon avatar = rs.getBytes("avatar") == null ? new ImageIcon(getClass().getResource("/icon_login/user.png")) : new ImageIcon(rs.getBytes("avatar"));
                account = new Account(accountId, username, password, AccountType.valueOf(accType), avatar);
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return accounts;
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
                Icon avatar = rs.getBytes("avatar") == null ? new ImageIcon(getClass().getResource("/icon_login/user.png")) : new ImageIcon(rs.getBytes("avatar"));
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
