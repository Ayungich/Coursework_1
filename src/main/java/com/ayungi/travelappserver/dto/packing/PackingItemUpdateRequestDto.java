package com.ayungi.travelappserver.dto.packing;

public class PackingItemUpdateRequestDto {
    private Boolean taken;

    public PackingItemUpdateRequestDto() { }

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
