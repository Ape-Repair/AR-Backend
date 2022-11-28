package com.aperepair.aperepair.resources.aws.gateway.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aperepair.aperepair.domain.exception.AwsS3ImageException;
import com.aperepair.aperepair.domain.exception.AwsServiceInternalException;
import com.aperepair.aperepair.domain.gateway.ReceiptOrderGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

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

    private static final Logger logger = LogManager.getLogger(ReceiptOrderGatewayImpl.class.getName());
}