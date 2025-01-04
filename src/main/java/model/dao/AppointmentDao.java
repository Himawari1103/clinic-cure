package model.dao;

import constants.AccountType;
import model.base.Account;
import model.base.Appointment;
import model.db_connection.DBConnection;
import util.Utils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AppointmentDao implements Dao<Appointment> {
    public static AppointmentDao getInstance() {
        return new AppointmentDao();
    }

    private String appointmentId;
    private String patientId;
    private String staffId;
    private LocalDateTime time;

    @Override
    public int insert(Appointment value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "INSERT INTO appointments (appointmentId, patientId, staffId, time) VALUES (?, ?, ?, ?);";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, value.getAppointmentId());
            stmt.setString(2, value.getPatientId());
            stmt.setString(3, value.getStaffId());
            stmt.setTimestamp(4, Utils.localDateTimeToSqlTimestamp(value.getTime()));

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int update(Appointment value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "UPDATE appointments SET patientId = ?, staffId = ?, time = ? WHERE appointmentId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(4, value.getAppointmentId());
            stmt.setString(1, value.getPatientId());
            stmt.setString(2, value.getStaffId());
            stmt.setTimestamp(3, Utils.localDateTimeToSqlTimestamp(value.getTime()));

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int delete(Appointment value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        try {
            String sql = "DELETE FROM appointments WHERE appointmentId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, value.getAppointmentId());

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public Appointment selectById(String id) {
        Appointment appointment = null;
        Connection c = DBConnection.getConnection();

        try {
            String sql = "SELECT *  FROM appointments WHERE appointmentId = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String appointmentId = rs.getString("appointmentId");
                String patientId = rs.getString("patientId");
                String staffId = rs.getString("staffId");
                LocalDateTime time = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("time"));
                appointment = new Appointment(appointmentId, patientId, staffId, time);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return appointment;
    }

    @Override
    public ArrayList<Appointment> selectAll() {
        ArrayList<Appointment> appointments = new ArrayList<>();
        Connection c = DBConnection.getConnection();

        try {
            String sql = "SELECT *  FROM appointments;";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = null;
                String appointmentId = rs.getString("appointmentId");
                String patientId = rs.getString("patientId");
                String staffId = rs.getString("staffId");
                LocalDateTime time = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("time"));
                appointment = new Appointment(appointmentId, patientId, staffId, time);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return appointments;
    }

    @Override
    public ArrayList<Appointment> selectByCondition(String condition) {
        return null;
    }
}
