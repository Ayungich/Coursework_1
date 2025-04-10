package com.ayungi.travelappserver.dto.registration;

public class RegistrationResponseDto {
    private long userId;

    public RegistrationResponseDto() {
    }

    public RegistrationResponseDto(long userId) {
        this.userId = userId;
    }

    public long getUserId() {return userId;}
    public void setUserId(long userId) {this.userId = userId;}
}
