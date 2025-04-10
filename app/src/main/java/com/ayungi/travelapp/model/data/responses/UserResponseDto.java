package com.ayungi.travelapp.model.data.responses;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class UserResponseDto {
    private Long id; // Уникальный идентификатор для каждого пользователя
    private String login; // Логин пользователя
    private String email; // Электронная почта
    private String password; // Пароль
    private String firstName; // Имя
    private String lastName; // Фамилия
    private String gender; // Пол
    private String dateOfBirth; // Дата рождения
    private Long avatarId; // ID аватара
    private Boolean isAvatarEmpty; // имеется ли аватар

    // Getters
    public Long getId() {return id;}
    public String getLogin() {return login;}
    public String getEmail() {return email;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getGender() {return gender;}
    public String getDateOfBirth() {return dateOfBirth;}
    public Long getAvatarId() {return avatarId;}
    public Boolean getIsAvatarEmpty() {return isAvatarEmpty;}
    public String getPassword() {return password;}

    // Setters
    public void setId(Long id) {this.id = id;}
    public void setLogin(String login) {this.login = login;}
    public void setEmail(String email) {this.email = email;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setGender(String gender) {this.gender = gender;}
    public void setDateOfBirth(String dateOfBirth) {this.dateOfBirth = dateOfBirth;}
    public void setAvatarId(Long avatarId) {this.avatarId = avatarId;}
    public void setIsAvatarEmpty(Boolean isAvatarEmpty) {this.isAvatarEmpty = isAvatarEmpty;}
    public void setPassword(String password) {this.password = password;}

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", avatarId=" + avatarId +
                ", isAvatarEmpty=" + isAvatarEmpty +
                '}';
    }
}
