package com.ayungi.travelapp.model.data.requests;

public class TripRequestDto {
    private String name;
    private String startDate; // Формат "dd.MM.yyyy"
    private String endDate;   // Формат "dd.MM.yyyy"
    private String type;

    public TripRequestDto() { }

    public TripRequestDto(String name, String startDate, String endDate, String type) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
