package com.example.yourtree;

// 노트 클래스
public class Note {

    Integer noteNum;
    String noteTitle;
    String noteContent;
    String noteName;
    String noteDate;

    // 생성자
    public Note(Integer noteNum, String noteTitle, String noteContent, String name, String date) {
        this.noteNum = noteNum;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteName = name;
        this.noteDate = date;
    }
    public Integer getNoteNum() { return noteNum; }

    public void setNoteNum(Integer noteNum) { this.noteNum = noteNum; }

    public String getNoteTitle() { return noteTitle; }

    public void setNoteTitle(String Title) { this.noteTitle = noteTitle; }

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
