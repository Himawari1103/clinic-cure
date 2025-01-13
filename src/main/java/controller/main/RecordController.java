package controller.main;

import model.base.Record;
import model.dao.*;
import model.db_connection.DBConnection;
import util.Utils;
import view.home.components.table.Table;
import view.home.main.model.ModelRecord;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class RecordController {
    public static void addRowRecordTable(Table table, HashMap<String, ModelRecord> mapModelRecord) {
        Connection c = DBConnection.getConnection();
        try {
            String sql = "SELECT \n" +
                    "    r.recordId,\n" +
                    "    r.patientId, \n" +
                    "    p.fullName AS patientName, \n" +
                    "    r.staffId, \n" +
                    "    s.fullName AS doctorName, \n" +
                    "    r.symptom, \n" +
                    "    r.diagnosis, \n" +
                    "    r.prescription, \n" +
                    "    r.createdAt AS recordCreatedAt,\n" +
                    "    CASE \n" +
                    "        WHEN rc.receiptId IS NOT NULL THEN TRUE\n" +
                    "        ELSE FALSE\n" +
                    "    END AS paymentStatus\n" +
                    "FROM \n" +
                    "    records r\n" +
                    "JOIN \n" +
                    "    patients p ON r.patientId = p.patientId\n" +
                    "JOIN \n" +
                    "    staffs s ON r.staffId = s.staffId\n" +
                    "LEFT JOIN \n" +
                    "    receipts rc ON r.recordId = rc.recordId\n" +
                    "ORDER BY \n" +
                    "    r.createdAt;\n\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String recordId = rs.getString("recordId");
                String patientId = rs.getString("patientId");
                String patientName = rs.getString("patientName");
                String staffId = rs.getString("staffId");
                String doctorName = rs.getString("doctorName");
                String symptom = rs.getString("symptom");
                String diagnosis = rs.getString("diagnosis");
                String prescription = rs.getString("prescription");
                LocalDateTime recordCreatedAt = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("recordCreatedAt"));
                boolean paymentStatus = rs.getBoolean("paymentStatus");

                table.addRow(new String[]{
                        recordId, patientId, patientName, doctorName, Utils.localDateTimeToStringWithTime(recordCreatedAt), paymentStatus ? "Đã thanh toán" : "Chưa thanh toán"
                });

                ModelRecord modelRecord = new ModelRecord(new Record(recordId, patientId, staffId, symptom, diagnosis, prescription, recordCreatedAt), patientName, staffId, paymentStatus);
                mapModelRecord.put(modelRecord.getRecordId(), modelRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.closeConnection(c);
    }

    public static ArrayList<ModelRecord> addRowRecordTableDialog(Table table, boolean isPaid) {
        Connection c = DBConnection.getConnection();
        ArrayList<ModelRecord> modelRecords = new ArrayList<>();
        try {
            String sql = "SELECT \n" +
                    "    r.recordId,\n" +
                    "    r.patientId, \n" +
                    "    p.fullName AS patientName, \n" +
                    "    r.staffId, \n" +
                    "    s.fullName AS doctorName, \n" +
                    "    r.symptom, \n" +
                    "    r.diagnosis, \n" +
                    "    r.prescription, \n" +
                    "    r.createdAt AS recordCreatedAt,\n" +
                    "    CASE \n" +
                    "        WHEN rc.receiptId IS NOT NULL THEN TRUE\n" +
                    "        ELSE FALSE\n" +
                    "    END AS paymentStatus\n" +
                    "FROM \n" +
                    "    records r\n" +
                    "JOIN \n" +
                    "    patients p ON r.patientId = p.patientId\n" +
                    "JOIN \n" +
                    "    staffs s ON r.staffId = s.staffId\n" +
                    "LEFT JOIN \n" +
                    "    receipts rc ON r.recordId = rc.recordId\n" +
                    "ORDER BY \n" +
                    "    r.createdAt;\n\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String recordId = rs.getString("recordId");
                String patientId = rs.getString("patientId");
                String patientName = rs.getString("patientName");
                String staffId = rs.getString("staffId");
                String doctorName = rs.getString("doctorName");
                String symptom = rs.getString("symptom");
                String diagnosis = rs.getString("diagnosis");
                String prescription = rs.getString("prescription");
                LocalDateTime recordCreatedAt = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("recordCreatedAt"));
                boolean paymentStatus = rs.getBoolean("paymentStatus");

                System.out.println("Record ID: " + recordId);
                System.out.println("Patient ID: " + patientId);
                System.out.println("Patient Name: " + patientName);
                System.out.println("Staff ID: " + staffId);
                System.out.println("Doctor Name: " + doctorName);
                System.out.println("Symptom: " + symptom);
                System.out.println("Diagnosis: " + diagnosis);
                System.out.println("Prescription: " + prescription);
                System.out.println("Record Created At: " + recordCreatedAt);
                System.out.println("Payment Status: " + (paymentStatus ? "Paid" : "Unpaid"));
                System.out.println("-------------------------------");

                table.addRow(new String[]{
                        recordId, patientId, patientName, doctorName, Utils.localDateTimeToStringWithTime(recordCreatedAt), paymentStatus ? "Đã thanh toán" : "Chưa thanh toán"
                });

                ModelRecord modelRecord = new ModelRecord(new Record(recordId, patientId, staffId, symptom, diagnosis, prescription, recordCreatedAt), patientName, staffId, paymentStatus);

                if (!isPaid) {
                    if (!paymentStatus) {
                        modelRecords.add(modelRecord);
                    }
                } else {
                    modelRecords.add(modelRecord);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.closeConnection(c);

        return modelRecords;
    }

    public static ArrayList<ModelRecord> addRowRecordTable(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);
        Connection c = DBConnection.getConnection();
        ArrayList<ModelRecord> modelRecords = new ArrayList<>();
        try {
            String sql = "SELECT \n" +
                    "    r.recordId,\n" +
                    "    r.patientId, \n" +
                    "    p.fullName AS patientName, \n" +
                    "    r.staffId, \n" +
                    "    s.fullName AS doctorName, \n" +
                    "    r.symptom, \n" +
                    "    r.diagnosis, \n" +
                    "    r.prescription, \n" +
                    "    r.createdAt AS recordCreatedAt,\n" +
                    "    CASE \n" +
                    "        WHEN rc.receiptId IS NOT NULL THEN TRUE\n" +
                    "        ELSE FALSE\n" +
                    "    END AS paymentStatus\n" +
                    "FROM \n" +
                    "    records r\n" +
                    "JOIN \n" +
                    "    patients p ON r.patientId = p.patientId\n" +
                    "JOIN \n" +
                    "    staffs s ON r.staffId = s.staffId\n" +
                    "LEFT JOIN \n" +
                    "    receipts rc ON r.recordId = rc.recordId\n" +
                    "ORDER BY \n" +
                    "    r.createdAt;\n\n";
            PreparedStatement stmt = c.prepareStatement(sql);

            System.out.println(stmt.toString());

            // Thực hiện truy vấn và nhận kết quả
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String recordId = rs.getString("recordId");
                String patientId = rs.getString("patientId");
                String patientName = rs.getString("patientName");
                String staffId = rs.getString("staffId");
                String doctorName = rs.getString("doctorName");
                String symptom = rs.getString("symptom");
                String diagnosis = rs.getString("diagnosis");
                String prescription = rs.getString("prescription");
                LocalDateTime recordCreatedAt = Utils.sqlTimestampToLocalDateTime(rs.getTimestamp("recordCreatedAt"));
                boolean paymentStatus = rs.getBoolean("paymentStatus");

                System.out.println("Record ID: " + recordId);
                System.out.println("Patient ID: " + patientId);
                System.out.println("Patient Name: " + patientName);
                System.out.println("Staff ID: " + staffId);
                System.out.println("Doctor Name: " + doctorName);
                System.out.println("Symptom: " + symptom);
                System.out.println("Diagnosis: " + diagnosis);
                System.out.println("Prescription: " + prescription);
                System.out.println("Record Created At: " + recordCreatedAt);
                System.out.println("Payment Status: " + (paymentStatus ? "Paid" : "Unpaid"));
                System.out.println("-------------------------------");

                defaultTableModel.addRow(new String[]{
                        recordId, patientId, patientName, doctorName, Utils.localDateTimeToStringWithTime(recordCreatedAt), paymentStatus ? "Đã thanh toán" : "Chưa thanh toán"
                });

                ModelRecord modelRecord = new ModelRecord(new Record(recordId, patientId, staffId, symptom, diagnosis, prescription, recordCreatedAt), patientName, staffId, paymentStatus);
                modelRecords.add(modelRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.closeConnection(c);

        return modelRecords;
    }

    public static boolean addRecord(Record record, Table table) {
        int rs = RecordDao.getInstance().insert(record);
        if (rs != 0) {
            table.addRow(new String[]{
                    record.getRecordId(),
                    record.getPatientId(),
                    PatientDao.getInstance().selectById(record.getPatientId()).getFullName(),
                    StaffDao.getInstance().selectById(record.getStaffId()).getFullName(),
                    Utils.localDateTimeToStringWithTime(record.getCreatedAt()),
                    "Chưa thanh toán"
            });
            return true;
        }
        return false;
    }

    public static boolean updateRecord(int index, Record record, Table table) {
        int rs = RecordDao.getInstance().update(record);
        if (rs != 0) {
            table.updateRow(index, new String[]{
                    record.getRecordId(),
                    record.getPatientId(),
                    PatientDao.getInstance().selectById(record.getPatientId()).getFullName(),
                    StaffDao.getInstance().selectById(record.getStaffId()).getFullName(),
                    Utils.localDateTimeToStringWithTime(record.getCreatedAt()),
                    "Chưa thanh toán"
            });
            return true;
        }
        return false;
    }

    public static boolean payRecord(int index, Record record, Table table) {
        int rs = RecordDao.getInstance().update(record);
        if (rs != 0) {
            table.updateRow(index, new String[]{
                    record.getRecordId(),
                    record.getPatientId(),
                    PatientDao.getInstance().selectById(record.getPatientId()).getFullName(),
                    StaffDao.getInstance().selectById(record.getStaffId()).getFullName(),
                    Utils.localDateTimeToStringWithTime(record.getCreatedAt()),
                    "Đã thanh toán"
            });
            return true;
        }
        return false;
    }

    public static boolean deleteRecord(int index, Record record, Table table) {
        int rs = RecordDao.getInstance().delete(record);
        if (rs != 0) {
            table.deleteRow(index);
            return true;
        }
        return false;
    }
}
