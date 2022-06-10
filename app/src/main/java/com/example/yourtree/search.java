package com.example.yourtree;

public class search {

    String SfriendID;
    String SfriendName;
    String SfriendSTime;

    // 생성자
    public search(String sfriendID, String sfriendName, String sfriendSTime) {
        SfriendID = sfriendID;
        SfriendName = sfriendName;
        SfriendSTime = sfriendSTime;
    }

    // getter and setter
    public String getSfriendID() {
        return SfriendID;
    }

    public void setSfriendID(String sfriendID) {
        SfriendID = sfriendID;
    }

    public String getSfriendName() {
        return SfriendName;
    }

    public void setSfriendName(String sfriendName) {
        SfriendName = sfriendName;
    }

    public String getSfriendSTime() {
        return SfriendSTime;
    }

    public void setSfriendSTime(String sfriendSTime) {
        SfriendSTime = sfriendSTime;
    }
}
