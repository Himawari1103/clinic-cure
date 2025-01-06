package controller.main;

import model.base.Patient;
import model.dao.PatientDao;
import view.components.main.components.table.Table;

import java.util.ArrayList;

public class PatientController {
    public static void addRowPatientTable(Table table) {
        PatientDao.getInstance().selectAll().forEach(e -> table.addRow(e.toObjects()));
    }

    public static boolean addPatient(Patient patient, Table table) {
        int rs = PatientDao.getInstance().insert(patient);
        if (rs != 0){
            table.addRow(patient.toObjects());
            return true;
        }
        return false;
    }

    public static boolean updatePatient(int index, Patient patient, Table table) {
        int rs = PatientDao.getInstance().update(patient);
        if (rs != 0){
            table.updateRow(index, patient.toObjects());
            return true;
        }
        return false;
    }

    public static boolean deletePatient(int index, Patient patient, Table table) {
        int rs = PatientDao.getInstance().delete(patient);
        if (rs != 0){
            table.deleteRow(index);
            return true;
        }
        return false;
    }
}
