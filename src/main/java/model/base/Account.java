package model.base;


import constants.AccountType;

import javax.swing.*;
import java.time.LocalDateTime;

public class Account {
    private String accountId;
    private String username;
    private String password;
    private String email;
    private AccountType accountType;
    private LocalDateTime createdAt;
    private Icon avatar;

    public Account() {
    }

    public Account(String accountId, String username, String password, String email, AccountType accountType, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }

    public Account(String accountId, String username, String password, String email, AccountType accountType, LocalDateTime createdAt, Icon avatar) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.accountType = accountType;
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountType=" + accountType +
                ", avatar=" + avatar +
                '}';
    }
}
