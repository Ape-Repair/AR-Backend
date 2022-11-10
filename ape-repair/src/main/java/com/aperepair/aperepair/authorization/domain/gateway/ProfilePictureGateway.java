package com.aperepair.aperepair.authorization.domain.gateway;

import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;

import java.io.IOException;

public interface ProfilePictureGateway {

    boolean profilePictureCreation(ProfilePictureCreationRequestDto request) throws IOException;

    String getProfilePicture(GetProfilePictureRequestDto request) throws IOException;
}
