package com.aperepair.aperepair.resources.aws.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class GetProfilePictureRequestDto {

    @NotBlank
    @Email
    private String email;

    public GetProfilePictureRequestDto() {
        super();
    }

    public GetProfilePictureRequestDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
