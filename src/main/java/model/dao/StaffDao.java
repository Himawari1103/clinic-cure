package model.dao;

import constants.StaffRole;
import constants.StaffSpeciality;
import model.base.Record;
import model.base.Staff;
import model.db_connection.DBConnection;
import util.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class StaffDao implements Dao<Staff> {
    public static StaffDao getInstance() {
        return new StaffDao();
    }

    @Override
    public int insert(Staff value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        String sql = "INSERT INTO staffs (staffId, fullName, phoneNumber, email, role, speciality) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getStaffId());
            stmt.setString(2, value.getFullName());
            stmt.setString(3, value.getPhoneNumber());
            stmt.setString(4, value.getEmail());
            stmt.setString(5, value.getRole().toString());
            stmt.setString(6, value.getSpeciality().toString());

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int update(Staff value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        String sql = "UPDATE staffs SET fullName = ?, phoneNumber = ?, email = ?, role = ?, speciality = ? WHERE staffId = ?;";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(6, value.getStaffId());
            stmt.setString(1, value.getFullName());
            stmt.setString(2, value.getPhoneNumber());
            stmt.setString(3, value.getEmail());
            stmt.setString(4, value.getRole().toString());
            stmt.setString(5, value.getSpeciality().toString());

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public int delete(Staff value) {
        Connection c = DBConnection.getConnection();
        int rs = 0;
        String sql = "DELETE FROM staffs WHERE staffId = ?;";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, value.getStaffId());

            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return rs;
    }

    @Override
    public Staff selectById(String id) {
        Connection c = DBConnection.getConnection();
        Staff staff = null;
        String sql = "SELECT * FROM staffs WHERE staffId = ?;";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String staffId = rs.getString("staffId");
                String fullName = rs.getString("fullName");
                String phoneNumber = rs.getString("phoneNumber");
                String email = rs.getString("email");
                StaffRole role = StaffRole.valueOf(rs.getString("role"));
                StaffSpeciality speciality = StaffSpeciality.valueOf(rs.getString("speciality"));

                staff = new Staff(staffId, fullName, phoneNumber, email, role, speciality);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return staff;
    }

    @Override
    public ArrayList<Staff> selectAll() {
        ArrayList<Staff> staffs = new ArrayList<>();
        Connection c = DBConnection.getConnection();
        String sql = "SELECT * FROM staffs;";

        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Staff staff = null;

                String staffId = rs.getString("staffId");
                String fullName = rs.getString("fullName");
                String phoneNumber = rs.getString("phoneNumber");
                String email = rs.getString("email");
                StaffRole role = StaffRole.valueOf(rs.getString("role"));
                StaffSpeciality speciality = StaffSpeciality.valueOf(rs.getString("speciality"));

                staff = new Staff(staffId, fullName, phoneNumber, email, role, speciality);

                staffs.add(staff);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return staffs;
    }

    @Override
    public ArrayList<Staff> selectByCondition(String condition) {
        return null;
    }
}
