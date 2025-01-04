package model.dao;

import util.Utils;
import constants.Gender;
import model.base.Patient;
import model.db_connection.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PatientDao implements Dao<Patient> {
    public static PatientDao getInstance() {
        return new PatientDao();
    }

    @Override
    public int insert(Patient value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "insert into patients (patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address) values = (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getPatientId());
            stmt.setString(2, value.getFullName());
            stmt.setDate(3, Utils.localDateToSqlDate(value.getDateOfBirth()));
            stmt.setString(4, value.getGender().toString());
            stmt.setString(5, value.getPhoneNumber());
            stmt.setString(6, value.getNation());
            stmt.setString(7, value.getOccupation());
            stmt.setString(8, value.getAddress());
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int update(Patient value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "UPDATE patients SET fullName = ?, dateOfBirth = ?, gender = ?, phoneNumber = ?, nation = ?, occupation = ?, address = ? WHERE patientId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getFullName());
            stmt.setDate(2, Utils.localDateToSqlDate(value.getDateOfBirth()));
            stmt.setString(3, value.getGender().toString());
            stmt.setString(4, value.getPhoneNumber());
            stmt.setString(5, value.getNation());
            stmt.setString(6, value.getOccupation());
            stmt.setString(7, value.getAddress());
            stmt.setString(8, value.getPatientId());
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int delete(Patient value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "DELETE FROM patients WHERE patientId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getPatientId());
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public Patient selectById(String id) {
        Patient patient = null;
        Connection c = DBConnection.getConnection();

        try {
            String sql = "select * from patients WHERE patientId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String patientId = rs.getString("patientId");
                String fullName = rs.getString("fullName");
                LocalDate dateOfBirth = Utils.sqlDateToLocalDate(rs.getDate("dateOfBirth"));
                Gender gender = Gender.valueOf(rs.getString("gender"));
                String phoneNumber = rs.getString("phoneNumber");
                String nation = rs.getString("nation");
                String occupation = rs.getString("occupation");
                String address = rs.getString("address");

                patient = new Patient(patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return patient;
    }

    @Override
    public ArrayList<Patient> selectAll() {
        ArrayList<Patient> patients = new ArrayList<>();
        Connection c = DBConnection.getConnection();

        try {
            String sql = "select * from patients;";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient patient = null;
                String patientId = rs.getString("patientId");
                String fullName = rs.getString("fullName");
                LocalDate dateOfBirth = Utils.sqlDateToLocalDate(rs.getDate("dateOfBirth"));
                Gender gender = Gender.valueOf(rs.getString("gender"));
                String phoneNumber = rs.getString("phoneNumber");
                String nation = rs.getString("nation");
                String occupation = rs.getString("occupation");
                String address = rs.getString("address");

                patient = new Patient(patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address);

                patients.add(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return patients;
    }

    @Override
    public ArrayList<Patient> selectByCondition(String condition) {
        return null;
    }
}
