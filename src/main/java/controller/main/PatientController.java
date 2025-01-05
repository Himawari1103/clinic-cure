package controller.main;

import model.dao.PatientDao;
import view.components.main.components.table.Table;

import java.util.ArrayList;

public class PatientController {
    public static void addRowPatientTable(Table table){
        PatientDao.getInstance().selectAll().forEach(e -> table.addRow(e.toObjects()));
    }
}
