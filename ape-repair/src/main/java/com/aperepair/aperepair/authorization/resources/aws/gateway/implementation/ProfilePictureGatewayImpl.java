package com.aperepair.aperepair.authorization.resources.aws.gateway.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.aperepair.aperepair.authorization.domain.exception.AwsS3ImageException;
import com.aperepair.aperepair.authorization.domain.exception.AwsServiceInternalException;
import com.aperepair.aperepair.authorization.domain.exception.AwsUploadException;
import com.aperepair.aperepair.authorization.domain.gateway.ProfilePictureGateway;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ProfilePictureGatewayImpl implements ProfilePictureGateway {

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public boolean profilePictureCreation(
            ProfilePictureCreationRequestDto request
    ) throws AwsUploadException, IOException {
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
        } catch (Exception ex) {
            logger.error(String.format("Failed to upload image of email: [%s] - in AWS Bucket S3",
                    request.getEmail()));
            throw new AwsUploadException(ex.getMessage());
        }
        finally {
            assert imageInputStream != null;
            imageInputStream.close();
        }
    }

    @Override
    public String getProfilePicture(GetProfilePictureRequestDto request) throws IOException, AwsS3ImageException, AwsServiceInternalException {
        S3ObjectInputStream s3ImageInputStream = null;

        try {
            S3Object imageFile = amazonS3.getObject("ar-profile-pictures",
                    (request.getEmail() + "/image.png"));

            s3ImageInputStream = imageFile.getObjectContent();

            byte[] imageByteArray = IOUtils.toByteArray(s3ImageInputStream);
            String base64 = java.util.Base64.getEncoder().encodeToString(imageByteArray);

            logger.info(String.format("Base64 image successfully obtained of: [%s]", request.getEmail()));

            return base64;
        } catch (AmazonS3Exception ex) {
            logger.error("[Failed: AwsS3ImageException] - error in some Amazon S3 service");
            throw new AwsS3ImageException(ex.getMessage());
        } catch (AmazonServiceException ex) {
            logger.error("[Failed: AwsServiceInternalException] - error in some amazon service");
            throw new AwsServiceInternalException(ex.getMessage());
        } catch (IOException ex) {
            logger.error("[Failed: IOException] - error at bucket flow");
            throw ex;
        } finally {
            if (s3ImageInputStream == null) {
                logger.info("Don't have image to close");
            } else {
                s3ImageInputStream.close();
            }
        }
    }

    private static final Logger logger = LogManager.getLogger(ProfilePictureGatewayImpl.class.getName());
}
