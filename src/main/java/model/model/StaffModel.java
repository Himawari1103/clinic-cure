package model.model;

import constants.AccountType;
import model.base.*;
import model.base.Record;
import model.dao.*;

import java.util.ArrayList;

public class StaffModel {
    private static ArrayList<Patient> patients;
    private static ArrayList<Record> records;
    private static ArrayList<Staff> staffs;
    private static ArrayList<Appointment> appointments;
    private static ArrayList<Receipt> receipts;

    public StaffModel() {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.STAFF) {
            patients = PatientDao.getInstance().selectAll();
            records = RecordDao.getInstance().selectAll();
            staffs = StaffDao.getInstance().selectAll();
            appointments = AppointmentDao.getInstance().selectAll();
            receipts = ReceiptDao.getInstance().selectAll();
        }
    }

    public static void reset(){
        patients = null;
        records = null;
        staffs = null;
        appointments = null;
        receipts = null;
    }

    public static ArrayList<Patient> getPatients() {
        return patients;
    }

    public static void setPatients(ArrayList<Patient> patients) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            StaffModel.patients = patients;
        }
    }

    public static ArrayList<model.base.Record> getRecords() {
        return records;
    }

    public static void setRecords(ArrayList<Record> records) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            StaffModel.records = records;
        }
    }

    public static ArrayList<Staff> getStaffs() {
        return staffs;
    }

    public static void setStaffs(ArrayList<Staff> staffs) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            StaffModel.staffs = staffs;
        }
    }

    public static ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public static void setAppointments(ArrayList<Appointment> appointments) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            StaffModel.appointments = appointments;
        }
    }

    public static ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public static void setReceipts(ArrayList<Receipt> receipts) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            StaffModel.receipts = receipts;
        }
    }
}
