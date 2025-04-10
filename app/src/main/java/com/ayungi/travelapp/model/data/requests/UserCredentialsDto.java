package com.ayungi.travelapp.model.data.requests;

public class UserCredentialsDto {
    private String login;
    private String email;
    private String password;
    private String gender;

    public UserCredentialsDto() { }

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
