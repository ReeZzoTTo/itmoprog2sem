package com.andreysankov.itmoprog2sem.client;

public class UserCredentials {
    private String login;
    private String password;

    public UserCredentials(String login, String password) {
        this.setCredentials(login, password);
    }

    public void setCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }

    public String getLogin() { return this.login; }
    public String getPassword() { return this.password; }
} 
