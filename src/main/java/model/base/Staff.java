package model.base;

import constants.StaffRole;
import constants.StaffSpeciality;

public class Staff {
    private String staffId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private StaffRole role;
    private StaffSpeciality speciality;

    public Staff() {
    }

    public Staff(String staffId, String fullName, String phoneNumber, String email, StaffRole role) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
    }

    public Staff(String staffId, String fullName, String phoneNumber, String email, StaffRole role, StaffSpeciality speciality) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
        this.speciality = speciality;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public StaffSpeciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(StaffSpeciality speciality) {
        this.speciality = speciality;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
