package com.example.yourtree;

// 노트 클래스
public class Note {

    Integer noteNum;
    String noteTitle;
    String noteContent;
    String noteWriter;
    String noteDate;
    String studytime;

    // 생성자
    public Note(Integer noteNum, String noteTitle, String noteContent, String writer, String date, String studytime) {
        this.noteNum = noteNum;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteWriter = writer;
        this.noteDate = date;
        this.studytime = studytime;
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

    public String getNoteWriter() {
        return noteWriter;
    }

    public void setNoteWriter(String noteWriter) {
        this.noteWriter = noteWriter;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getStudytime() {
        return studytime;
    }

    public void setStudytime(String studytime) {
        this.studytime = studytime;
    }
}
