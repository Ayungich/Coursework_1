package com.ayungi.travelappserver.dto.authorization;

public class AuthorizationResponseDto {

    private long userId;

    public AuthorizationResponseDto() {
    }

    public AuthorizationResponseDto(long userId) {
        this.userId = userId;
    }

    public long getUserId() {return userId;}
    public void setUserId(long userId) {this.userId = userId;}
}
