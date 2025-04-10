package com.ayungi.travelappserver.dto.coordinate;

public class CoordinateDto {
    private Long id;
    private double latitude;
    private double longitude;
    private String note; // Новое поле для заметки

    public CoordinateDto() { }

    public CoordinateDto(Long id, double latitude, double longitude, String note) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
    }

    public CoordinateDto(Long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CoordinateDto(double latitude, double longitude, String note) {
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
