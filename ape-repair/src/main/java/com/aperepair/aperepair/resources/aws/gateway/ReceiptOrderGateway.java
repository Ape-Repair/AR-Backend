package com.aperepair.aperepair.resources.aws.gateway;

import com.aperepair.aperepair.application.exception.AwsS3ImageException;
import com.aperepair.aperepair.application.exception.AwsServiceInternalException;
import com.aperepair.aperepair.application.exception.NotFoundException;

import java.io.File;
import java.io.InputStream;

public interface ReceiptOrderGateway {

    void uploadReceiptOrder(File file, String key, String email) throws AwsServiceInternalException;

    InputStream getReceiptOrder(String key, String email) throws AwsS3ImageException, AwsServiceInternalException, NotFoundException;
}