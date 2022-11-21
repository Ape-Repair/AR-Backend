package com.aperepair.aperepair.application.dto.response;

public class DeleteResponseDto {

    boolean success;

    public DeleteResponseDto(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
