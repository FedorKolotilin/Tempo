package com.example.version_2_backend.BackEnd;

public class Account {

    public static User accountUser = null;

    public static void setAccountUser(User accountUser) {
        Account.accountUser = accountUser;
    }
    public static User getAccountUser(){
        return accountUser;
    }
}
