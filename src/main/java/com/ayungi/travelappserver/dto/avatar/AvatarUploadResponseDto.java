package com.ayungi.travelappserver.dto.avatar;

public class AvatarUploadResponseDto {

    private boolean success;
    private String message;
    private Long avatarId;

    public AvatarUploadResponseDto() {
    }

    public AvatarUploadResponseDto(boolean success, String message, Long avatarId) {
        this.success = success;
        this.message = message;
        this.avatarId = avatarId;
    }

    public boolean isSuccess() {return success;}
    public void setSuccess(boolean success) {this.success = success;}

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}

    public Long getAvatarId() {return avatarId;}
    public void setAvatarId(Long avatarId) {this.avatarId = avatarId;}
}
