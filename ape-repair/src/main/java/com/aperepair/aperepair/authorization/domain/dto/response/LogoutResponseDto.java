package com.aperepair.aperepair.authorization.domain.dto.response;

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
