package com.ayungi.travelapp.model.data.requests;

public class DiaryRequestDto {
    private String title;
    private String date;
    private String content;

    public DiaryRequestDto() {
    }

    public DiaryRequestDto(String title, String date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }

    // Getters & Setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
