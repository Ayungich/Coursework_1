package com.ayungi.travelapp.model.data.requests;

public class RegistrationRequestDto {
    private String login;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;       // "male", "female", null
    private String dateOfBirth;

    public RegistrationRequestDto() {
    }

    public RegistrationRequestDto(String login, String email, String password,
                                  String firstName, String lastName, String gender,
                                  String dateOfBirth) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public String getLogin() {return login;}
    public void setLogin(String login) {this.login = login;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}

    public String getDateOfBirth() {return dateOfBirth;}
    public void setDateOfBirth(String dateOfBirth) {this.dateOfBirth = dateOfBirth;}
}
