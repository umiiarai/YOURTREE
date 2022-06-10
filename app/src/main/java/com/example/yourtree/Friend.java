package com.example.yourtree;

import android.graphics.drawable.Drawable;
import android.media.Image;

public class Friend {
    String userID;
    String userPassword;
    String userName;
    String userBirth;

    // 생성자
    public Friend(){}

    public Friend(String friendID, String friendPassword, String friendName, String friendSTime) {
        this.userID = friendID;
        this.userPassword = friendPassword;
        this.userName = friendName;
        this.userBirth = friendSTime;
    }

    // getter and setter
    public String getUserID() {
        return userID;
    }

    public void setUserID(String friendID) {
        friendID = friendID;
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

    public void setUserName(String friendName) {
        friendName = friendName;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String friendSTime) {
        friendSTime = friendSTime;
    }


}
