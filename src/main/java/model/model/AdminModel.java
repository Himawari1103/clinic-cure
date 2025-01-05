package model.model;

import constants.AccountType;
import model.base.*;
import model.base.Record;
import model.dao.*;

import java.util.ArrayList;

public class AdminModel {
    private static ArrayList<Patient> patients;
    private static ArrayList<Record> records;
    private static ArrayList<Staff> staffs;
    private static ArrayList<Appointment> appointments;
    private static ArrayList<Receipt> receipts;
    private static ArrayList<Account> accounts;

    public AdminModel() {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            patients = PatientDao.getInstance().selectAll();
            records = RecordDao.getInstance().selectAll();
            staffs = StaffDao.getInstance().selectAll();
            appointments = AppointmentDao.getInstance().selectAll();
            receipts = ReceiptDao.getInstance().selectAll();
            accounts = AccountDao.getInstance().selectAll();
        }
    }

    public static void reset(){
        patients = null;
        records = null;
        staffs = null;
        appointments = null;
        receipts = null;
        accounts = null;
    }

    public static ArrayList<Patient> getPatients() {
        return patients;
    }

    public static void setPatients(ArrayList<Patient> patients) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            AdminModel.patients = patients;
        }
    }

    public static ArrayList<Record> getRecords() {
        return records;
    }

    public static void setRecords(ArrayList<Record> records) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            AdminModel.records = records;
        }
    }

    public static ArrayList<Staff> getStaffs() {
        return staffs;
    }

    public static void setStaffs(ArrayList<Staff> staffs) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            AdminModel.staffs = staffs;
        }
    }

    public static ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public static void setAppointments(ArrayList<Appointment> appointments) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            AdminModel.appointments = appointments;
        }
    }

    public static ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public static void setReceipts(ArrayList<Receipt> receipts) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            AdminModel.receipts = receipts;
        }
    }

    public static ArrayList<Account> getAccounts() {
        return accounts;
    }

    public static void setAccounts(ArrayList<Account> accounts) {
        if (AccountModel.getAccount() != null && AccountModel.getAccount().getAccountType() == AccountType.ADMIN) {
            AdminModel.accounts = accounts;
        }
    }
}
