package view.home.main.model;

import constants.Gender;
import model.base.Patient;
import util.Utils;

import java.time.LocalDate;

public class ModelPatient extends Patient {
    private int age;

    public ModelPatient() {
    }

    public ModelPatient(String patientId, String fullName, LocalDate dateOfBirth, Gender gender, String phoneNumber, String nation, String occupation, String address) {
        super(patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address);
        this.age = LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public ModelPatient(Patient patient) {
        super(patient.getPatientId(), patient.getFullName(), patient.getDateOfBirth(), patient.getGender(), patient.getPhoneNumber(), patient.getNation(), patient.getOccupation(), patient.getAddress());
        this.age = LocalDate.now().getYear() - patient.getDateOfBirth().getYear();
    }

    public int getAge() {
        return age;
    }

    public static String[] getColumnNames() {
        return new String[]{
                "ID", "Họ tên", "Tuổi", "Ngày sinh", "Giới tính", "Điện thoại", "Dân tộc", "Nghề nghiệp", "Địa chỉ"
        };
    }

    public static ModelPatient getInstanceFromRow(String[] row) {
        String patientId = row[0];
        String fullName = row[0];
        LocalDate dateOfBirth = Utils.stringToLocalDate(row[0]);
        Gender gender = Gender.valueOf(row[0]);
        String phoneNumber = row[0];
        String nation = row[0];
        String occupation = row[0];
        String address = row[0];
        return new ModelPatient(patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address);
    }


}
