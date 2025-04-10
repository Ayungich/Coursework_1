package com.ayungi.travelappserver.dto.trip;

import com.ayungi.travelappserver.dto.coordinate.CoordinateDto;

import java.util.List;

public class TripRequestDto {
    private String name;
    private String startDate;
    private String endDate;
    private String type;
    private List<CoordinateDto> coordinates; // список координат

    public TripRequestDto() { }

    public TripRequestDto(String name,
                          String startDate,
                          String endDate,
                          String type,
                          List<CoordinateDto> coordinates) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.coordinates = coordinates;
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

    public List<CoordinateDto> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(List<CoordinateDto> coordinates) {
        this.coordinates = coordinates;
    }
}
