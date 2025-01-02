package model.base;

import java.time.LocalDateTime;

public class Record {
    private String recordId;
    private String patientId;
    private String doctorId;
    private String symptom;
    private String diagnosis;
    private String prescription;
    private LocalDateTime createdAt;

    public Record() {
    }

    public Record(String recordId, String patientId, String doctorId, String symptom, String diagnosis, String prescription) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.symptom = symptom;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.createdAt = LocalDateTime.now();
    }

    public Record(String recordId, String patientId, String doctorId, String symptom, String diagnosis, String prescription, LocalDateTime createdAt) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.symptom = symptom;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.createdAt = createdAt;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}