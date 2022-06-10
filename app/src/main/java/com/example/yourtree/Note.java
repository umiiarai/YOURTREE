package com.example.yourtree;

// 노트 클래스
public class Note {

    // int noteContent;
    String noteContent;
    String noteName;
    String noteDate;

    // 생성자
    public Note(String note, String name, String date) {
        this.noteContent = note;
        this.noteName = name;
        this.noteDate = date;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }
}
