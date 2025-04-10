package com.ayungi.travelapp.model.data.requests;

public class PackingItemUpdateRequestDto {
    private Boolean taken;

    public PackingItemUpdateRequestDto() {}

    public PackingItemUpdateRequestDto(Boolean taken) {
        this.taken = taken;
    }

    public Boolean getTaken() {
        return taken;
    }
    public void setTaken(Boolean taken) {
        this.taken = taken;
    }
}
