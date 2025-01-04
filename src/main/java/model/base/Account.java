package model.base;


import constants.AccountType;

import javax.swing.*;

public class Account {
    private String accountId;
    private String username;
    private String password;
    private AccountType accountType;
    private Icon avatar;

    public Account() {
    }

    public Account(String accountId, String username, String password, AccountType accountType) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public Account(String accountId, String username, String password, AccountType accountType, Icon avatar) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        if (avatar != null) {
            this.avatar = avatar;
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Icon getAvatar() {
        return avatar;
    }

    public void setAvatar(Icon avatar) {
        this.avatar = avatar;
    }
}
