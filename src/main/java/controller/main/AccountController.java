package controller.main;

import model.base.Account;

import model.dao.AccountDao;
import view.frames.MainView;

public class AccountController {
    public static MainView mainView;

    public static void test(){

    }

    public static void addAccount(Account account){
        AccountDao.getInstance().insert(account);
    }

    public static void updateAccount(Account account){
        AccountDao.getInstance().update(account);
    }

    public static void deleteAccount(Account account){
        AccountDao.getInstance().delete(account);
    }


}
