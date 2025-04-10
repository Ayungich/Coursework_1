package com.ayungi.travelappserver.dto.packing;

public class PackingItemEditRequestDto {
    private String name;
    private Boolean taken;

    public PackingItemEditRequestDto() { }

    public PackingItemEditRequestDto(String name, Boolean taken) {
        this.name = name;
        this.taken = taken;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTaken() {
        return taken;
    }
    public void setTaken(Boolean taken) {
        this.taken = taken;
    }
}
