package com.aperepair.aperepair.resources.aws.gateway.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.aperepair.aperepair.application.exception.AwsS3ImageException;
import com.aperepair.aperepair.application.exception.AwsServiceInternalException;
import com.aperepair.aperepair.application.exception.NotFoundException;
import com.aperepair.aperepair.resources.aws.gateway.ReceiptOrderGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Service
public class ReceiptOrderGatewayImpl implements ReceiptOrderGateway {

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public void uploadReceiptOrder(File receipt, String key, String email) throws AwsServiceInternalException {
        try {
            amazonS3.putObject(new PutObjectRequest("ar-receipt-order", key, receipt));

            logger.info(String.format("Receipt order created successfully for email: [%s]", email));
        } catch (SdkClientException ex) {
            throw new AwsServiceInternalException(ex.getMessage());
        }
    }

    public InputStream getReceiptOrder(String key, String email) throws AwsS3ImageException, AwsServiceInternalException, NotFoundException {
        try {
            S3Object receiptFile = amazonS3.getObject("ar-receipt-order", key);

            InputStream receiptInputStream = receiptFile.getObjectContent();

            logger.info(String.format("Receipt successfully obtained of: [%s]", email));

            return receiptInputStream;
        } catch (AmazonS3Exception ex) {
            logger.error("[Failed: AwsS3ImageException] - error in some Amazon S3 service");
            throw new AwsS3ImageException(ex.getMessage());
        } catch (AmazonServiceException ex) {
            logger.error("[Failed: AwsServiceInternalException] - error in some Amazon service");
            throw new AwsServiceInternalException(ex.getMessage());
        }
    }

    private static final Logger logger = LogManager.getLogger(ReceiptOrderGatewayImpl.class.getName());
}