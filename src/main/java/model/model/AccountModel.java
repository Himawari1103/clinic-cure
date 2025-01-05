package model.model;

import model.base.Account;
import model.dao.AccountDao;

public class AccountModel {
    private static Account account;

    public static void create (Account account) {
        if(AccountModel.account == null) {
            AccountModel.account = account;
        }
    }

    public static void reset(){
        account = null;
    }

    public static Account getAccount() {
        return account;
    }
}
