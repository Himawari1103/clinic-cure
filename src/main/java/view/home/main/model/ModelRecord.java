package view.home.main.model;

import constants.Gender;
import model.base.Record;
import util.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ModelRecord extends Record {
    private String patientName;
    private String staffName;
    private boolean paymentStatus;

    public ModelRecord() {
    }

    public ModelRecord(String recordId, String patientId, String staffId, String symptom, String diagnosis, String prescription, String patientName, String staffName, boolean paymentStatus) {
        super(recordId, patientId, staffId, symptom, diagnosis, prescription);
        this.patientName = patientName;
        this.staffName = staffName;
        this.paymentStatus = paymentStatus;
    }

    public ModelRecord(String recordId, String patientId, String staffId, String symptom, String diagnosis, String prescription, LocalDateTime createdAt, String patientName, String staffName, boolean paymentStatus) {
        super(recordId, patientId, staffId, symptom, diagnosis, prescription, createdAt);
        this.patientName = patientName;
        this.staffName = staffName;
        this.paymentStatus = paymentStatus;
    }

    public ModelRecord(Record record, String patientName, String staffName, boolean paymentStatus) {
        super(record.getRecordId(), record.getPatientId(), record.getStaffId(), record.getSymptom(), record.getDiagnosis(), record.getPrescription(), record.getCreatedAt());
        this.patientName = patientName;
        this.staffName = staffName;
        this.paymentStatus = paymentStatus;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public static String[] getColumnNames() {
        return new String[]{
                "ID", "ID bệnh nhân", "Tên bệnh nhân", "Tên bác sĩ", "Ngày khám", "Trạng thái"
        };
    }

//    public static ModelRecord getInstanceFromRow(String[] row) {
//        String recordId = row[0];
//        String patientId = row[0];
//        String staffId = row[0];
//        String symptom = row[0];
//        String diagnosis = row[0];
//        String prescription = row[0];
//        LocalDateTime createdAt = Utils.stringToLocalDateTimeWithTime(row[0]);
//        return new ModelRecord(recordId, patientId, staffId, symptom, diagnosis, prescription, createdAt);
//    }
}
