package com.ayungi.travelappserver.dto.packing;

public class PackingItemDto {
    private Long id;
    private String name;
    private Boolean taken;

    public PackingItemDto() { }

    public PackingItemDto(Long id, String name, Boolean taken) {
        this.id = id;
        this.name = name;
        this.taken = taken;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getTaken() { return taken; }
    public void setTaken(Boolean taken) { this.taken = taken; }
}
