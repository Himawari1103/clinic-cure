package controller.main;

import model.base.Appointment;
import model.base.Patient;
import model.dao.AppointmentDao;
import model.dao.PatientDao;
import model.dao.StaffDao;
import model.db_connection.DBConnection;
import util.Utils;
import view.components.main.components.table.Table;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AppointmentController {
    public static void addRowAppointmentTable(Table table) {
        Connection c = DBConnection.getConnection();
        try {
            String sql = "SELECT \n" +
                    "    a.appointmentId, \n" +
                    "    a.patientId, \n" +
                    "    p.fullName AS patientName, \n" +
                    "    a.staffId, \n" +
                    "    s.fullName AS staffName, \n" +
                    "    a.time AS appointmentTime\n" +
                    "FROM \n" +
                    "    appointments a\n" +
                    "JOIN \n" +
                    "    patients p ON a.patientId = p.patientId\n" +
                    "JOIN \n" +
                    "    staffs s ON a.staffId = s.staffId\n" +
                    "ORDER BY \n" +
                    "    a.time;\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String appointmentId = rs.getString("appointmentId");
                String patientId = rs.getString("patientId");
                String patientName = rs.getString("patientName");
                String staffId = rs.getString("staffId");
                String staffName = rs.getString("staffName");
                LocalDateTime appointmentTime = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("appointmentTime"));

                String[] row = {appointmentId, patientId, patientName, staffId, staffName, Utils.localDateTimeToStringWithTime(appointmentTime)};

                System.out.println("Appointment ID: " + appointmentId);
                System.out.println("Patient ID: " + patientId);
                System.out.println("Patient Name: " + patientName);
                System.out.println("Staff ID: " + staffId);
                System.out.println("Staff Name: " + staffName);
                System.out.println("Appointment Time: " + appointmentTime);
                System.out.println("-------------------------------");

                table.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
    }

    public static void addRowAppointmentTable(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);
        Connection c = DBConnection.getConnection();
        try {
            String sql = "SELECT \n" +
                    "    a.appointmentId, \n" +
                    "    a.patientId, \n" +
                    "    p.fullName AS patientName, \n" +
                    "    a.staffId, \n" +
                    "    s.fullName AS staffName, \n" +
                    "    a.time AS appointmentTime\n" +
                    "FROM \n" +
                    "    appointments a\n" +
                    "JOIN \n" +
                    "    patients p ON a.patientId = p.patientId\n" +
                    "JOIN \n" +
                    "    staffs s ON a.staffId = s.staffId\n" +
                    "ORDER BY \n" +
                    "    a.time;\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String appointmentId = rs.getString("appointmentId");
                String patientId = rs.getString("patientId");
                String patientName = rs.getString("patientName");
                String staffId = rs.getString("staffId");
                String staffName = rs.getString("staffName");
                LocalDateTime appointmentTime = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("appointmentTime"));

                String[] row = {appointmentId, patientId, patientName, staffId, staffName, Utils.localDateTimeToStringWithTime(appointmentTime)};

                System.out.println("Appointment ID: " + appointmentId);
                System.out.println("Patient ID: " + patientId);
                System.out.println("Patient Name: " + patientName);
                System.out.println("Staff ID: " + staffId);
                System.out.println("Staff Name: " + staffName);
                System.out.println("Appointment Time: " + appointmentTime);
                System.out.println("-------------------------------");

                defaultTableModel.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
    }

    public static boolean addAppointment(Appointment appointment, Table table) {
        int rs = AppointmentDao.getInstance().insert(appointment);
        if (rs != 0){
            table.addRow(new String[]{
                    appointment.getAppointmentId(),
                    appointment.getPatientId(),
                    PatientDao.getInstance().selectById(appointment.getPatientId()).getFullName(),
                    appointment.getStaffId(),
                    StaffDao.getInstance().selectById(appointment.getStaffId()).getFullName(),
                    Utils.localDateTimeToStringWithTime(appointment.getTime())
            });
            return true;
        }
        return false;
    }

    public static boolean updateAppointment(int index, Appointment appointment, Table table) {
        int rs = AppointmentDao.getInstance().update(appointment);
        if (rs != 0){
            table.updateRow(index, new String[]{
                    appointment.getAppointmentId(),
                    appointment.getPatientId(),
                    PatientDao.getInstance().selectById(appointment.getPatientId()).getFullName(),
                    appointment.getStaffId(),
                    StaffDao.getInstance().selectById(appointment.getStaffId()).getFullName(),
                    Utils.localDateTimeToStringWithTime(appointment.getTime())
            });
            return true;
        }
        return false;
    }

    public static boolean deleteAppointment(int index, Appointment appointment, Table table) {
        int rs = AppointmentDao.getInstance().delete(appointment);
        if (rs != 0){
            table.deleteRow(index);
            return true;
        }
        return false;
    }

    public static Patient selectPatient(){
        return new Patient();
    }
}
