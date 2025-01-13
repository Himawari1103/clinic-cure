package view.home.main.model;

import model.base.Appointment;
import util.Utils;

import java.time.LocalDateTime;

public class ModelAppointment extends Appointment {
    String patientName;
    String staffName;

    public ModelAppointment() {
    }

    public ModelAppointment(String patientName, String staffName) {
        this.patientName = patientName;
        this.staffName = staffName;
    }

    public ModelAppointment(String appointmentId, String patientId, String staffId, LocalDateTime time, String patientName, String staffName) {
        super(appointmentId, patientId, staffId, time);
        this.patientName = patientName;
        this.staffName = staffName;
    }

    public ModelAppointment(Appointment appointment, String patientName, String staffName) {
        super(appointment.getAppointmentId(), appointment.getPatientId(), appointment.getStaffId(), appointment.getTime());
        this.patientName = patientName;
        this.staffName = staffName;
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

    public static String[] getColumnNames() {
        return new String[]{"ID", "ID bệnh nhân", "Tên bệnh nhân", "ID bác sĩ", "Tên bác sĩ", "Thời gian"};
    }

    public String[] toRow(){
        String appointmentId = this.getAppointmentId();
        String patientId = this.getPatientId();
        String patientName = this.patientName;
        String staffId = this.getStaffId();
        String staffName = this.staffName;
        LocalDateTime time = this.getTime();
        return new String[]{appointmentId, patientId, staffId, Utils.localDateTimeToStringWithTime(time), patientName, staffName};
    }

    public static ModelAppointment getInstanceFromRow(String[] row) {
        String appointmentId = row[0];
        String patientId = row[1];
        String patientName = row[2];
        String staffId = row[3];
        String staffName = row[4];
        LocalDateTime time = Utils.stringToLocalDateTimeWithTime(row[5]);
        return new ModelAppointment(appointmentId, patientId, staffId, time, patientName, staffName);
    }
}
