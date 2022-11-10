package com.aperepair.aperepair.authorization.resources.aws.gateway.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.gateway.ProfilePictureGateway;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ProfilePictureGatewayImpl implements ProfilePictureGateway {

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

    //TODO: Criar exception handler para retornar os erros mapeados, invés de 500;
    @Override
    public String getProfilePicture(GetProfilePictureRequestDto request) throws IOException {
        S3ObjectInputStream s3ImageInputStream = null;

        try {
            S3Object imageFile = amazonS3.getObject("ar-profile-pictures",
                    (request.getEmail() + "/image.png"));

            s3ImageInputStream = imageFile.getObjectContent();

            byte[] imageByteArray = IOUtils.toByteArray(s3ImageInputStream);
            String base64 = java.util.Base64.getEncoder().encodeToString(imageByteArray);

            logger.info(String.format("Base64 image successfully obtained of: [%s]", request.getEmail()));

            return base64;
        }
        catch (FileNotFoundException ex) {
            logger.error(String.format("[Failed: FileNotFoundException] - of email: [%s]", request.getEmail()));
            throw ex;
        }
        catch (AmazonServiceException ex) {
            logger.error("[Failed: AmazonServiceException] - error in some amazon service");
            throw ex;
        }
        catch (IOException ex) {
            logger.error("[Failed: IOException] - error at bucket flow");
            throw ex;
        } finally {
            assert s3ImageInputStream != null;
            s3ImageInputStream.close();
        }
    }

    private static final Logger logger = LogManager.getLogger(ProfilePictureGatewayImpl.class.getName());
}
