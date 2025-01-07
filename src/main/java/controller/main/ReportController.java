package controller.main;

import model.base.Staff;
import model.dao.StaffDao;
import model.db_connection.DBConnection;
import util.Utils;
import view.components.main.components.table.Table;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReportController {
    public static void addDataDailyReport(Table table, LocalDate start, LocalDate end) {
        Connection c = DBConnection.getConnection();
        try {
            String sql = "SELECT \n" +
                    "    DATE(rc.createdAt) AS date,\n" +
                    "    SUM(rc.amount) AS total_revenue,\n" +
                    "    COUNT(r.recordId) AS total_visits\n" +
                    "FROM \n" +
                    "    records r\n" +
                    "JOIN \n" +
                    "    receipts rc ON r.recordId = rc.recordId\n" +
                    "WHERE \n" +
                    "    rc.createdAt BETWEEN ? AND ?"+
                    "GROUP BY \n" +
                    "    DATE(rc.createdAt)\n" +
                    "ORDER BY \n" +
                    "    DATE(rc.createdAt);\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            // Thực hiện thay đổi các tham số ngày (thí dụ: từ '2024-01-01' đến '2024-02-01')
            stmt.setString(1, start.getYear()+"-"+ start.getMonthValue() + "-" + start.getDayOfMonth());
            stmt.setString(2, end.getYear()+"-"+ end.getMonthValue() + "-" + end.getDayOfMonth());

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDateTime date = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("date"));
                double total_revenue = rs.getDouble("total_revenue");
                int total_visits = rs.getInt("total_visits");

                String[] row = {Utils.localDateTimeToString(date), String.valueOf(total_revenue), String.valueOf(total_visits)};

                System.out.println("date: " + date);
                System.out.println("total_revenue: " + total_revenue);
                System.out.println("total_visits: " + total_visits);
                System.out.println("============");

                table.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
    }

    public static void addDataDailyReport(Table table) {
        Connection c = DBConnection.getConnection();
        try {
            String sql = "SELECT \n" +
                    "    DATE(rc.createdAt) AS date,\n" +
                    "    SUM(rc.amount) AS total_revenue,\n" +
                    "    COUNT(r.recordId) AS total_visits\n" +
                    "FROM \n" +
                    "    records r\n" +
                    "JOIN \n" +
                    "    receipts rc ON r.recordId = rc.recordId\n" +
                    "GROUP BY \n" +
                    "    DATE(rc.createdAt)\n" +
                    "ORDER BY \n" +
                    "    DATE(rc.createdAt);\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDateTime date = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("date"));
                double total_revenue = rs.getDouble("total_revenue");
                int total_visits = rs.getInt("total_visits");

                String[] row = {Utils.localDateTimeToString(date), String.valueOf(total_revenue), String.valueOf(total_visits)};

                System.out.println("date: " + date);
                System.out.println("total_revenue: " + total_revenue);
                System.out.println("total_visits: " + total_visits);
                System.out.println("============");

                table.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
    }

    public static void addDataPatientList(Table table, LocalDate start, LocalDate end) {
        System.out.println("debug!");
        Connection c = DBConnection.getConnection();
        try {
            String sql = "SELECT \n" +
                    "    p.patientId, \n" +
                    "    p.fullName, \n" +
                    "    p.dateOfBirth, \n" +
                    "    p.phoneNumber, \n" +
                    "    p.address,\n" +
                    "    COUNT(r.recordId) AS numberOfVisits\n" +
                    "FROM \n" +
                    "    patients p\n" +
                    "LEFT JOIN \n" +
                    "    records r ON p.patientId = r.patientId\n" +
                    "WHERE \n" +
                    "    r.createdAt BETWEEN ? AND ?\n" +
                    "GROUP BY \n" +
                    "    p.patientId, p.fullName, p.dateOfBirth, p.phoneNumber, p.address\n" +
                    "ORDER BY \n" +
                    "    p.patientId;\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            // Thực hiện thay đổi các tham số ngày (thí dụ: từ '2024-01-01' đến '2024-02-01')
            stmt.setString(1, start.getYear()+"-"+ start.getMonthValue() + "-" + start.getDayOfMonth());
            stmt.setString(2, end.getYear()+"-"+ end.getMonthValue() + "-" + end.getDayOfMonth());

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            // Lặp qua kết quả và hiển thị
            while (rs.next()) {
                String patientId = rs.getString("patientId");
                String fullName = rs.getString("fullName");
                LocalDate dateOfBirth = Utils.sqlDateToLocalDate(rs.getDate("dateOfBirth"));
                String phoneNumber = rs.getString("phoneNumber");
                String address = rs.getString("address");
                int numberOfVisits = rs.getInt("numberOfVisits");

                String[] row = {patientId, fullName, Utils.localDateToString(dateOfBirth), phoneNumber, address, String.valueOf(numberOfVisits)};

                System.out.println("Patient ID: " + patientId);
                System.out.println("Full Name: " + fullName);
                System.out.println("Date of Birth: " + dateOfBirth);
                System.out.println("Phone Number: " + phoneNumber);
                System.out.println("Address: " + address);
                System.out.println("Number of Visits: " + numberOfVisits);
                System.out.println("-------------------------------");

                table.addRow(row);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
    }

    public static void addDataPatientList(Table table) {
        System.out.println("debug!");
        Connection c = DBConnection.getConnection();
        try {
            String sql = "SELECT \n" +
                    "    p.patientId, \n" +
                    "    p.fullName, \n" +
                    "    p.dateOfBirth, \n" +
                    "    p.phoneNumber, \n" +
                    "    p.address,\n" +
                    "    COUNT(r.recordId) AS numberOfVisits\n" +
                    "FROM \n" +
                    "    patients p\n" +
                    "LEFT JOIN \n" +
                    "    records r ON p.patientId = r.patientId\n" +
                    "GROUP BY \n" +
                    "    p.patientId, p.fullName, p.dateOfBirth, p.phoneNumber, p.address\n" +
                    "ORDER BY \n" +
                    "    p.patientId;\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            // Lặp qua kết quả và hiển thị
            while (rs.next()) {
                String patientId = rs.getString("patientId");
                String fullName = rs.getString("fullName");
                LocalDate dateOfBirth = Utils.sqlDateToLocalDate(rs.getDate("dateOfBirth"));
                String phoneNumber = rs.getString("phoneNumber");
                String address = rs.getString("address");
                int numberOfVisits = rs.getInt("numberOfVisits");

                String[] row = {patientId, fullName, Utils.localDateToString(dateOfBirth), phoneNumber, address, String.valueOf(numberOfVisits)};

                System.out.println("Patient ID: " + patientId);
                System.out.println("Full Name: " + fullName);
                System.out.println("Date of Birth: " + dateOfBirth);
                System.out.println("Phone Number: " + phoneNumber);
                System.out.println("Address: " + address);
                System.out.println("Number of Visits: " + numberOfVisits);
                System.out.println("-------------------------------");

                table.addRow(row);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
    }

    public static double computeSumIncome(LocalDate start, LocalDate end){
        Connection c = DBConnection.getConnection();
        double sumIncome = 0;
        try {
            String sql = "SELECT \n" +
                    "    SUM(rc.amount) AS total_revenue\n" +
                    "FROM \n" +
                    "    receipts rc\n" +
                    "JOIN \n" +
                    "    records r ON rc.recordId = r.recordId\n" +
                    "WHERE \n" +
                    "    r.createdAt BETWEEN ? AND ?;\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, start.getYear()+"-"+ start.getMonthValue() + "-" + start.getDayOfMonth());
            stmt.setString(2, end.getYear()+"-"+ end.getMonthValue() + "-" + end.getDayOfMonth());

            System.out.println(stmt.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sumIncome = rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return sumIncome;
    }

    public static double computeSumIncome(){
        Connection c = DBConnection.getConnection();
        double sumIncome = 0;
        try {
            String sql = "SELECT \n" +
                    "    SUM(rc.amount) AS total_revenue\n" +
                    "FROM \n" +
                    "    receipts rc\n" +
                    "JOIN \n" +
                    "    records r ON rc.recordId = r.recordId;\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sumIncome = rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return sumIncome;
    }

    public static int computeSumVisit(LocalDate start, LocalDate end){
        Connection c = DBConnection.getConnection();
        int sumVisit = 0;
        try {
            String sql = "SELECT \n" +
                    "    COUNT(r.recordId) AS total_visits\n" +
                    "FROM \n" +
                    "    records r\n" +
                    "JOIN \n" +
                    "    receipts rc ON r.recordId = rc.recordId\n" +
                    "WHERE \n" +
                    "    r.createdAt BETWEEN ? AND ?;\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, start.getYear()+"-"+ start.getMonthValue() + "-" + start.getDayOfMonth());
            stmt.setString(2, end.getYear()+"-"+ end.getMonthValue() + "-" + end.getDayOfMonth());

            System.out.println(stmt.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sumVisit = rs.getInt("total_visits");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return sumVisit;
    }

    public static int computeSumVisit(){
        Connection c = DBConnection.getConnection();
        int sumVisit = 0;
        try {
            String sql = "SELECT \n" +
                    "    COUNT(r.recordId) AS total_visits\n" +
                    "FROM \n" +
                    "    records r\n" +
                    "JOIN \n" +
                    "    receipts rc ON r.recordId = rc.recordId;\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sumVisit = rs.getInt("total_visits");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBConnection.closeConnection(c);
        return sumVisit;
    }
}
