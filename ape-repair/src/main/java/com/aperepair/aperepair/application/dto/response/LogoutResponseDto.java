package com.aperepair.aperepair.application.dto.response;

public class LogoutResponseDto {

    private boolean success;

    public LogoutResponseDto(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
