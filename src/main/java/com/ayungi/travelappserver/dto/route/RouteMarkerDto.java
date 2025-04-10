package com.ayungi.travelappserver.dto.route;

public class RouteMarkerDto {
    private Long id;
    private Long tripId;
    private double latitude;
    private double longitude;
    private String title;
    private String description;

    public RouteMarkerDto() { }

    public RouteMarkerDto(Long id,
                          Long tripId,
                          double latitude,
                          double longitude,
                          String title,
                          String description) {
        this.id = id;
        this.tripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
