package com.aperepair.aperepair.authorization.application.dto.response;

import org.springframework.http.HttpStatus;

public class ApiExceptionResponseDto {

    private HttpStatus status;

    private String message;

    private String type;

    public ApiExceptionResponseDto(HttpStatus status, String message, String type) {
        this.status = status;
        this.message = message;
        this.type = type;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}