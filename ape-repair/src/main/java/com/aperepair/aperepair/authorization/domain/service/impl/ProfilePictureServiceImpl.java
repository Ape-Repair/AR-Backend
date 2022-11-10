package com.aperepair.aperepair.authorization.domain.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.aperepair.aperepair.authorization.application.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.service.ProfilePictureService;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public boolean profilePictureCreation(
            ProfilePictureCreationRequestDto request
    ) throws IOException {
        InputStream imageInputStream = null;

        try {
            byte[] imageByteArray = Base64.decodeBase64(request.getImage().getBytes());

            imageInputStream = new ByteArrayInputStream(imageByteArray);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");

            amazonS3.putObject(new PutObjectRequest("ar-profile-pictures",
                            (request.getEmail() + "/image.png"),
                            imageInputStream, metadata
                    )
            );

            logger.info(String.format("Profile picture created successfully for user email - [%s]",
                    request.getEmail()));
            return true;
        } catch (Exception e) {
            logger.error(String.format("Failed to update image of email: [%s] - in external bucket",
                    request.getEmail()));
            e.printStackTrace();
            throw e;
        } finally {
            assert imageInputStream != null;
            imageInputStream.close();
        }
    }

    //TODO: Adicionar logs e refatorar alguns pontos (está funcionando)
    @Override
    public String getProfilePicture(GetProfilePictureRequestDto request) throws IOException {
        S3ObjectInputStream s3ImageInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            S3Object imageFile = amazonS3.getObject("ar-profile-pictures",
                    (request.getEmail() + "/image.png"));

            s3ImageInputStream = imageFile.getObjectContent();

            byte[] teste = IOUtils.toByteArray(s3ImageInputStream);
            String base64 = java.util.Base64.getEncoder().encodeToString(teste);

            System.out.println(base64);

        }
        catch (FileNotFoundException e) {
            logger.error(String.format("[Failed: FileNotFoundException] - of email: [%s] - in external bucket",
                    request.getEmail()));
            throw new RuntimeException(e);
        }
        catch (AmazonServiceException e) {
            throw e;
        }
        catch (IOException e) {
            System.err.println(e.getMessage());

        } finally {
            assert s3ImageInputStream != null;
            s3ImageInputStream.close();
        }

        return "TESTANDO";
    }

    private static final Logger logger = LogManager.getLogger(ProfilePictureServiceImpl.class.getName());
}
