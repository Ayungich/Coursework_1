package com.ayungi.travelappserver.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserResponseDto {

    private long id;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private Long avatarId;
    private Boolean isAvatarEmpty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponseDto() {
    }

    public UserResponseDto(long id, String login, String email, String firstName, String lastName,
                           String gender, LocalDate dateOfBirth, Long avatarId, Boolean isAvatarEmpty,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.avatarId = avatarId;
        this.isAvatarEmpty = isAvatarEmpty;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getLogin() {return login;}
    public void setLogin(String login) {this.login = login;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}

    public LocalDate getDateOfBirth() {return dateOfBirth;}
    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}

    public Long getAvatarId() {return avatarId;}
    public void setAvatarId(Long avatarId) {this.avatarId = avatarId;}

    public Boolean getIsAvatarEmpty() {return isAvatarEmpty;}
    public void setIsAvatarEmpty(Boolean isAvatarEmpty) {this.isAvatarEmpty = isAvatarEmpty;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
}
