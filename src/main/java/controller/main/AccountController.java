package controller.main;

import model.base.Account;

import model.dao.AccountDao;
import view.components.main.components.table.Table;

import javax.swing.table.DefaultTableModel;

public class AccountController {

    public static void addRowAccountTable(Table table) {
        AccountDao.getInstance().selectAll().forEach(e -> table.addRow(e.toStrings()));
    }

    public static void addRowAccountTable(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);
        AccountDao.getInstance().selectAll().forEach(e -> defaultTableModel.addRow(e.toStrings()));
    }

    public static boolean addAccount(Account account, Table table) {
        int rs = AccountDao.getInstance().insert(account);
        if (rs != 0){
            table.addRow(account.toStrings());
            return true;
        }
        return false;
    }

    public static boolean addAccount(Account account, DefaultTableModel defaultTableModel) {
        int rs = AccountDao.getInstance().insert(account);
        if (rs != 0){
            defaultTableModel.addRow(account.toStrings());
            return true;
        }
        return false;
    }

    public static boolean updateAccount(int index, Account account, Table table) {
        int rs = AccountDao.getInstance().update(account);
        if (rs != 0){
            table.updateRow(index, account.toStrings());
            return true;
        }
        return false;
    }

    public static boolean deleteAccount(int index, Account account, Table table) {
        int rs = AccountDao.getInstance().delete(account);
        if (rs != 0){
            table.deleteRow(index);
            return true;
        }
        return false;
    }

}
