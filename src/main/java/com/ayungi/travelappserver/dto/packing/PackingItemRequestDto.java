package com.ayungi.travelappserver.dto.packing;

public class PackingItemRequestDto {
    private String name;
    private Boolean taken;
    private Long tripId; // Идентификатор путешествия

    public PackingItemRequestDto() { }

    public PackingItemRequestDto(String name, Boolean taken, Long tripId) {
        this.name = name;
        this.taken = taken;
        this.tripId = tripId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getTaken() { return taken; }
    public void setTaken(Boolean taken) { this.taken = taken; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
}
