package com.example.water.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AmazonS3Config {

    @Value("${amazon.aws.accesskey}")
    private String awsAccessKeyId;

    @Value("${amazon.aws.secretkey}")
    private String awsSecretKey;

    @Value("${amazon.aws.region}")
    private String awsRegion;

    @Value("${amazon.s3.bucketname}")
    private String bucketName; // 버킷 이름을 프로퍼티에서 가져옴

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
        return AmazonS3Client.builder()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
    @Bean
    public String bucketName() {
        return bucketName;
    }
}