package com.example.yourtree;

// 노트 클래스
public class Note {

    String note;
    String name;
    String date;

    // 생성자
    public Note(String note, String name, String date) {
        this.note = note;
        this.name = name;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String notice) {
        this.note = notice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
