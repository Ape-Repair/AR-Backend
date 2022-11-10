package com.aperepair.aperepair.authorization.resources.aws.dto.response;

public class ProfilePictureCreationResponseDto {

    private boolean success;

    public ProfilePictureCreationResponseDto(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
