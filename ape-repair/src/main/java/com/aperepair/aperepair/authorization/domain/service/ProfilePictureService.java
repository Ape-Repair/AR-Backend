package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.application.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.ProfilePictureCreationRequestDto;

import java.io.IOException;

public interface ProfilePictureService {

    boolean profilePictureCreation(ProfilePictureCreationRequestDto request) throws IOException;

    String getProfilePicture(GetProfilePictureRequestDto request) throws IOException;
}
