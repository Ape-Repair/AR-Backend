package com.aperepair.aperepair.authorization.resources.aws.dto.response;

public class GetProfilePictureResponseDto {

    private String imageBase64;

    public GetProfilePictureResponseDto(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
