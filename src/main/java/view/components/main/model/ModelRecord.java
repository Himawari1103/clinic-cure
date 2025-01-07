package view.components.main.model;

import model.base.Record;

public class ModelRecord {
    private Record record;
    private String patientName;
    private String staffName;
    private boolean paymentStatus;

    public ModelRecord() {
    }

    public ModelRecord(Record record, String patientName, String staffName, boolean paymentStatus) {
        this.record = record;
        this.patientName = patientName;
        this.staffName = staffName;
        this.paymentStatus = paymentStatus;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
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
}
