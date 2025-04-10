package com.ayungi.travelapp.model.data.responses;

public class CoordinateResponseDto {
    private Long id;
    private double latitude;
    private double longitude;
    private String note; // Новое поле для заметки

    public CoordinateResponseDto() { }

    public CoordinateResponseDto(Long id, double latitude, double longitude, String note) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
