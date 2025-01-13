package controller.main;

import constants.StaffRole;
import model.base.Staff;
import model.dao.StaffDao;
import view.home.components.table.Table;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;

public class StaffController {
    public static void addRowStaffTable(Table table, HashMap<String, Staff> mapStaff) {
        StaffDao.getInstance().selectAll().forEach(e -> {
            table.addRow(e.toObjects());
            mapStaff.put(e.getStaffId(),e);
        });
    }

    public static void addRowStaffIsDoctorTable(Table table) {
        StaffDao.getInstance().selectAll().forEach(
                e -> {
                    if(e.getRole() == StaffRole.DOCTOR){
                        table.addRow(new String[]{
                                e.getStaffId(),
                                e.getFullName(),
                                e.getSpeciality().getDetail(),
                                e.getPhoneNumber(),
                                e.getEmail()
                        });
                    }
                }
        );
    }

    public static void addRowStaffTable(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);

        StaffDao.getInstance().selectAll().forEach(e -> defaultTableModel.addRow(e.toObjects()));
    }

    public static boolean addStaff(Staff staff, Table table) {
        int rs = StaffDao.getInstance().insert(staff);
        if (rs != 0) {
            table.addRow(staff.toObjects());
            return true;
        }
        return false;
    }

    public static boolean updateStaff(int index, Staff staff, Table table) {
        int rs = StaffDao.getInstance().update(staff);
        if (rs != 0) {
            table.updateRow(index, staff.toObjects());
            return true;
        }
        return false;
    }

    public static boolean deleteStaff(int index, Staff staff, Table table) {
        int rs = StaffDao.getInstance().delete(staff);
        if (rs != 0) {
            table.deleteRow(index);
            return true;
        }
        return false;
    }
}
