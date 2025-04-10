package com.ayungi.travelappserver.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCredentialsDto {

    @NotBlank(message = "Login must not be blank")
    @Size(min = 5, max = 20, message = "Login length must be between 5 and 20 characters")
    private String login;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;

    // Пол не является обязательным
    private String gender;

    public UserCredentialsDto() {
    }

    public UserCredentialsDto(String login, String email, String password, String gender) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
