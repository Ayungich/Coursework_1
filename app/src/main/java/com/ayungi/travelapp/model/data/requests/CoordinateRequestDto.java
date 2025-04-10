package com.ayungi.travelapp.model.data.requests;

public class CoordinateRequestDto {
    private Long tripId;
    private double latitude;
    private double longitude;
    private String note; // Новое поле для заметки

    public CoordinateRequestDto() { }

    public CoordinateRequestDto(Long tripId, double latitude, double longitude, String note) {
        this.tripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
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
