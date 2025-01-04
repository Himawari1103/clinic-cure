package model.model;

import model.base.Account;
import model.dao.AccountDao;

public class AccountModel {
    private static Account account;

    public static void create (String accountId) {
        if(account == null) {
            account = AccountDao.getInstance().selectById(accountId);
        }
    }

    public static Account getAccount() {
        return account;
    }
}
