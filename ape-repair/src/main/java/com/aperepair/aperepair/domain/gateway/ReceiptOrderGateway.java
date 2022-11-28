package com.aperepair.aperepair.domain.gateway;

import com.aperepair.aperepair.domain.exception.AwsServiceInternalException;

import java.io.File;

public interface ReceiptOrderGateway {

    void uploadReceiptOrder(File file, String key, String email) throws AwsServiceInternalException;
}