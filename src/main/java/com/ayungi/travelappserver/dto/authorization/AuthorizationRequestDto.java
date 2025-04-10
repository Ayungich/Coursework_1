package com.ayungi.travelappserver.dto.authorization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthorizationRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public AuthorizationRequestDto() {
    }

    public AuthorizationRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}
