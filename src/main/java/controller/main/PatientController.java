package controller.main;

import model.base.Patient;
import model.base.Staff;
import model.dao.PatientDao;
import util.Utils;
import view.home.components.table.Table;
import view.home.main.model.ModelPatient;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;

public class PatientController {
    public static void addRowPatientTable(Table table, HashMap<String, ModelPatient> mapModelPatients) {
        PatientDao.getInstance().selectAll().forEach(e -> {
            table.addRow(e.toObjects());
            mapModelPatients.put(e.getPatientId(),new ModelPatient(e));
        });
    }

    public static void addRowPatientTableDialog(Table table) {
        PatientDao.getInstance().selectAll().forEach(
                e -> table.addRow(
                        new String[]{
                                e.getPatientId(),
                                e.getFullName(),
                                Utils.localDateToString(e.getDateOfBirth()),
                                e.getGender().getDetail(),
                                e.getPhoneNumber()
                        }
                )
        );
    }

    public static void addRowPatientTable(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);
        PatientDao.getInstance().selectAll().forEach(e -> defaultTableModel.addRow(e.toObjects()));
    }

    public static boolean addPatient(Patient patient, Table table) {
        int rs = PatientDao.getInstance().insert(patient);
        if (rs != 0){
            table.addRow(patient.toObjects());
            return true;
        }
        return false;
    }

    public static boolean addPatient(Patient patient, DefaultTableModel defaultTableModel) {
        int rs = PatientDao.getInstance().insert(patient);
        if (rs != 0){
            defaultTableModel.addRow(patient.toObjects());
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
