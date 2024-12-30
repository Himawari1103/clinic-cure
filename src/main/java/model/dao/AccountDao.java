package model.dao;

import model.Account;

import java.util.ArrayList;

public class AccountDao implements Dao<Account>{
    public static AccountDao getInstance(){
        return new AccountDao();
    }

    @Override
    public int insert(Account value) {
        return 0;
    }

    @Override
    public int update(Account value) {
        return 0;
    }

    @Override
    public int delete(Account value) {
        return 0;
    }

    @Override
    public Account selectById(String id) {
        return null;
    }

    @Override
    public ArrayList<Account> selectAll() {
        return null;
    }

    @Override
    public ArrayList<Account> selectByCondition(String condition) {
        return null;
    }
}
