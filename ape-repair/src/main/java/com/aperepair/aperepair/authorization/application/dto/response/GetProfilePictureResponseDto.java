package com.aperepair.aperepair.authorization.application.dto.response;

public class GetProfilePictureResponseDto {

    private String image;

    public GetProfilePictureResponseDto(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
