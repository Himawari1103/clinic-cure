package model.dao;

import model.base.Receipt;
import model.db_connection.DBConnection;
import util.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReceiptDao implements Dao<Receipt> {
    public static ReceiptDao getInstance() {
        return new ReceiptDao();
    }


    @Override
    public int insert(Receipt value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        String sql = "INSERT INTO receipts (receiptId, recordId, amount, createdAt) VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getReceiptId());
            stmt.setString(2, value.getRecordId());
            stmt.setDouble(3, value.getAmount());
            stmt.setTimestamp(4, Utils.localDateTimeToSqlTimestamp(value.getCreatedAt()));

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int update(Receipt value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        String sql = "UPDATE receipts SET recordId = ?, amount = ?, createdAt = ? WHERE receiptId = ?;";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(4, value.getReceiptId());
            stmt.setString(1, value.getRecordId());
            stmt.setDouble(2, value.getAmount());
            stmt.setTimestamp(3, Utils.localDateTimeToSqlTimestamp(value.getCreatedAt()));

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int delete(Receipt value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        String sql = "DELETE FROM receipts WHERE receiptId = ?;";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getReceiptId());

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public Receipt selectById(String id) {
        Connection c = DBConnection.getConnection();
        Receipt receipt = null;
        String sql = "SELECT * FROM receipts;";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String receiptId = rs.getString("receiptId");
                String recordId = rs.getString("recordId");
                double amount = rs.getDouble("amount");
                LocalDateTime createdAt = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("createdAt"));

                receipt = new Receipt(receiptId, recordId, amount, createdAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return receipt;
    }

    @Override
    public ArrayList<Receipt> selectAll() {
        Connection c = DBConnection.getConnection();
        ArrayList<Receipt> receipts = new ArrayList<>();
        String sql = "SELECT * FROM receipts;";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Receipt receipt = null;

                String receiptId = rs.getString("receiptId");
                String recordId = rs.getString("recordId");
                double amount = rs.getDouble("amount");
                LocalDateTime createdAt = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("createdAt"));

                receipt = new Receipt(receiptId, recordId, amount, createdAt);
                receipts.add(receipt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return receipts;
    }

    @Override
    public ArrayList<Receipt> selectByCondition(String condition) {
        return null;
    }
}
