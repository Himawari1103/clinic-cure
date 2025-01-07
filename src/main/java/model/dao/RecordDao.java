package model.dao;

import model.base.Record;
import model.db_connection.DBConnection;
import util.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RecordDao implements Dao<Record> {
    public static RecordDao getInstance() {
        return new RecordDao();
    }

    @Override
    public int insert(Record value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "INSERT INTO records (recordId, patientId, staffId, symptom, diagnosis, prescription, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, value.getRecordId());
            stmt.setString(2, value.getPatientId());
            stmt.setString(3, value.getStaffId());
            stmt.setString(4, value.getSymptom());
            stmt.setString(5, value.getDiagnosis());
            stmt.setString(6, value.getPrescription());
            stmt.setTimestamp(7, Utils.localDateTimeToSqlTimestamp(value.getCreatedAt()));

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int update(Record value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "UPDATE records SET patientId = ?, staffId = ?, symptom = ?, diagnosis = ?, prescription = ?, createdAt = ? WHERE recordId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(7, value.getRecordId());
            stmt.setString(1, value.getPatientId());
            stmt.setString(2, value.getStaffId());
            stmt.setString(3, value.getSymptom());
            stmt.setString(4, value.getDiagnosis());
            stmt.setString(5, value.getPrescription());
            stmt.setTimestamp(6, Utils.localDateTimeToSqlTimestamp(value.getCreatedAt()));

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int delete(Record value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "DELETE FROM records WHERE recordId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getRecordId());
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public Record selectById(String id) {
        Record record = null;
        Connection c = DBConnection.getConnection();

        try {
            String sql = "SELECT * FROM records WHERE recordId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String recordId = rs.getString("recordId");
                String patientId = rs.getString("patientId");
                String staffId = rs.getString("staffId");
                String symptom = rs.getString("symptom");
                String diagnosis = rs.getString("diagnosis");
                String prescription = rs.getString("prescription");
                LocalDateTime createdAt = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("createdAt"));

                record = new Record(recordId, patientId, staffId, symptom, diagnosis, prescription, createdAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return record;
    }

    @Override
    public ArrayList<Record> selectAll() {
        ArrayList<Record> records = new ArrayList<>();
        Connection c = DBConnection.getConnection();

        try {
            String sql = "SELECT * FROM records;";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Record record = null;

                String recordId = rs.getString("recordId");
                String patientId = rs.getString("patientId");
                String staffId = rs.getString("staffId");
                String symptom = rs.getString("symptom");
                String diagnosis = rs.getString("diagnosis");
                String prescription = rs.getString("prescription");
                LocalDateTime createdAt = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("createdAt"));

                record = new Record(recordId, patientId, staffId, symptom, diagnosis, prescription, createdAt);

                records.add(record);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return records;
    }

    @Override
    public ArrayList<Record> selectByCondition(String condition) {
        return null;
    }
}
