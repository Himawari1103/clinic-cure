package test;

import constants.AccountType;
import constants.Gender;
import constants.StaffRole;
import model.base.*;
import model.base.Record;
import model.dao.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.UUID;

public class TestDAO {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        AccountDao accountDao = AccountDao.getInstance();
        AppointmentDao appointmentDao = AppointmentDao.getInstance();
        PatientDao patientDao = PatientDao.getInstance();
        ReceiptDao receiptDao = ReceiptDao.getInstance();
        RecordDao recordDao = RecordDao.getInstance();
        StaffDao staffDao = StaffDao.getInstance();
        for (int i = 10; i < 20; i++) {
            String accountID = UUID.randomUUID().toString();
            accountDao.insert(new Account(accountID, String.valueOf(i), String.valueOf(i), AccountType.ADMIN));
//            accountDao.update(new Account(accountID, String.valueOf(i+10), String.valueOf(i+10), AccountType.STAFF));
//            accountDao.delete(new Account(accountID, String.valueOf(i), String.valueOf(i), AccountType.ADMIN));

            String patientId = UUID.randomUUID().toString();
            patientDao.insert(new Patient(patientId, String.valueOf(i), LocalDate.now(), Gender.MALE, String.valueOf(i), String.valueOf(i), String.valueOf(i), String.valueOf(i)));
//            patientDao.update(new Patient(patientId, String.valueOf(i+10), LocalDate.now(), Gender.FEMALE, String.valueOf(i+10), String.valueOf(i+10), String.valueOf(i+10), String.valueOf(i+10)));

            String staffId = UUID.randomUUID().toString();
            staffDao.insert(new Staff(staffId, String.valueOf(i), String.valueOf(i), String.valueOf(i), StaffRole.DOCTOR));
//            staffDao.update(new Staff(staffId, String.valueOf(i+10), String.valueOf(i+10), String.valueOf(i+10), StaffRole.STAFF));

            String appointmentId = UUID.randomUUID().toString();
            appointmentDao.insert(new Appointment(appointmentId, patientId, staffId, LocalDateTime.now()));
//            appointmentDao.update(new Appointment(appointmentId, patientId, staffId, LocalDateTime.now()));

            String recordId = UUID.randomUUID().toString();
            recordDao.insert(new Record(recordId, patientId, staffId, String.valueOf(i), String.valueOf(i), String.valueOf(i)));
//            recordDao.update(new Record(recordId, patientId, staffId, String.valueOf(i+10), String.valueOf(i+10), String.valueOf(i+10)));

            String receiptId = UUID.randomUUID().toString();
            receiptDao.insert(new Receipt(receiptId, recordId, 1000));
//            receiptDao.update(new Receipt(receiptId, recordId, 2000));
        }

        accountDao.selectAll().forEach(System.out::println);
        patientDao.selectAll().forEach(System.out::println);
        staffDao.selectAll().forEach(System.out::println);
        appointmentDao.selectAll().forEach(System.out::println);
        recordDao.selectAll().forEach(System.out::println);
        receiptDao.selectAll().forEach(System.out::println);

//        Account account = new Account();
//        account.setAccountId("1 or' 1=1 -- ");
//
//        Patient patient = new Patient();
//        patient.setPatientId("1 or' 1=1 -- ");
//
//        Staff staff = new Staff();
//        staff.setStaffId("1 or' 1=1 -- ");
//
//        Appointment appointment = new Appointment();
//        appointment.setAppointmentId("1 or' 1=1 -- ");
//
//        Record record = new Record();
//        record.setRecordId("1 or' 1=1 -- ");
//
//        Receipt receipt = new Receipt();
//        receipt.setReceiptId("1 or' 1=1 -- ");
//
//        accountDao.delete(account);
//        patientDao.delete(patient);
//        staffDao.delete(staff);
//        appointmentDao.delete(appointment);
//        recordDao.delete(record);
//        receiptDao.delete(receipt);
//
//        System.out.println("-----After Delete-----");
//        accountDao.selectAll().forEach(System.out::println);
//        patientDao.selectAll().forEach(System.out::println);
//        staffDao.selectAll().forEach(System.out::println);
//        appointmentDao.selectAll().forEach(System.out::println);
//        recordDao.selectAll().forEach(System.out::println);
//        receiptDao.selectAll().forEach(System.out::println);
    }
}
