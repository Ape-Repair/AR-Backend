package com.aperepair.aperepair.resources.aws.gateway;

import com.aperepair.aperepair.application.exception.AwsS3ImageException;
import com.aperepair.aperepair.application.exception.AwsServiceInternalException;
import com.aperepair.aperepair.application.exception.AwsUploadException;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;

import java.io.IOException;

public interface ProfilePictureGateway {

    boolean profilePictureCreation(ProfilePictureCreationRequestDto request) throws AwsUploadException, IOException;

    String getProfilePicture(GetProfilePictureRequestDto request) throws IOException, AwsS3ImageException, AwsServiceInternalException;
}
