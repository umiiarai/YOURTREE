package com.example.yourtree;

public class search {

    String userID;
    String userPassword;
    String userName;
    String userBirth;

    // 생성자
    public search() {}
    public search(String userID, String userPassword, String userName, String userBirth) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userBirth = userBirth;
    }

    // getter and setter
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }
}
