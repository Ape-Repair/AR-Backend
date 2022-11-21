package com.aperepair.aperepair.application.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.key}")
    private String key;

    @Value("${aws.secret}")
    private String secret;

    @Bean
    public AmazonS3 AutheticationS3() {

        logger.info("Authenticating on Amazon S3...");

        try {
            return AmazonS3ClientBuilder
                    .standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secret)))
                    .build();
        } catch (Exception e) {
            logger.error("Error authenticating on S3");
            throw e;
        }
    }

    private static final Logger logger = LogManager.getLogger(AwsS3Config.class.getName());
}