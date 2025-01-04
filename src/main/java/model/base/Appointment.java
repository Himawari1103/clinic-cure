package model.base;


import java.time.LocalDateTime;

public class Appointment {
    private String appointmentId;
    private String patientId;
    private String staffId;
    private LocalDateTime time;

    public Appointment() {
    }

    public Appointment(String appointmentId, String patientId, String staffId, LocalDateTime time) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.staffId = staffId;
        this.time = time;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
