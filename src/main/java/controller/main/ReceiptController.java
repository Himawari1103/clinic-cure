package controller.main;

import model.base.Receipt;
import model.dao.ReceiptDao;
import view.home.components.table.Table;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;

public class ReceiptController {
    public static void addRowReceiptTable(Table table, HashMap<String, Receipt> mapReceipt) {
        ReceiptDao.getInstance().selectAll().forEach(e -> {
            table.addRow(e.toStrings());
            mapReceipt.put(e.getReceiptId(),e);
        });
    }

    public static void addRowReceiptTable(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);
        ReceiptDao.getInstance().selectAll().forEach(e -> defaultTableModel.addRow(e.toStrings()));
    }

    public static boolean addReceipt(Receipt receipt, Table table) {
        int rs = ReceiptDao.getInstance().insert(receipt);
        if (rs != 0) {
            table.addRow(receipt.toStrings());
            return true;
        }
        return false;
    }

    public static boolean updateReceipt(int index, Receipt receipt, Table table) {
        int rs = ReceiptDao.getInstance().update(receipt);
        if (rs != 0) {
            table.updateRow(index, receipt.toStrings());
            return true;
        }
        return false;
    }

    public static boolean deleteReceipt(int index, Receipt receipt, Table table) {
        int rs = ReceiptDao.getInstance().delete(receipt);
        if (rs != 0) {
            table.deleteRow(index);
            return true;
        }
        return false;
    }
}
