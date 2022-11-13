package com.aperepair.aperepair.authorization.domain.gateway;

import com.aperepair.aperepair.authorization.domain.exception.AwsS3ImageException;
import com.aperepair.aperepair.authorization.domain.exception.AwsServiceInternalException;
import com.aperepair.aperepair.authorization.domain.exception.AwsUploadException;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;

import java.io.IOException;

public interface ProfilePictureGateway {

    boolean profilePictureCreation(ProfilePictureCreationRequestDto request) throws AwsUploadException, IOException;

    String getProfilePicture(GetProfilePictureRequestDto request) throws IOException, AwsS3ImageException, AwsServiceInternalException;
}
