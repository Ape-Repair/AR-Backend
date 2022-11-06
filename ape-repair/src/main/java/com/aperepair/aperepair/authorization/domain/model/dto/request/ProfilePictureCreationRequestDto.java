package com.aperepair.aperepair.authorization.domain.model.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ProfilePictureCreationRequestDto {

    @Email
    String email;

    @NotBlank
    String image;

    public ProfilePictureCreationRequestDto(String email, String image) {
        this.email = email;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}