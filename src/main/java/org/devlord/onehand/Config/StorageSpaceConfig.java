package org.devlord.onehand.Config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageSpaceConfig {

    @Value("${do.spaces.endpoint}")
    private String endpoint;

    @Value("${do.spaces.region}")
    private String region;

    @Value("${do.spaces.access-key}")
    private String accessKey;

    @Value("${do.spaces.secret-key}")
    private String secretKey;

    @Bean
    public AmazonS3 s3Client(){
        BasicAWSCredentials creds = new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint,region))
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
