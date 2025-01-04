package controller.login;

import constants.LoginResult;
import model.base.Account;
import model.dao.AccountDao;
import model.model.AccountModel;

public class LoginController {
    public static LoginResult login(String userName, String password){
        Account acc = AccountDao.getInstance().selectForLogin(userName, password);
        if (acc == null) {
            return LoginResult.USERNAME_ERR;
        } else if (acc.getUsername() == null) {
            return LoginResult.PASSWORD_ERR;
        } else {
            AccountModel.create(acc.getAccountId());
            return LoginResult.SUCCESS;
        }
    }
}
