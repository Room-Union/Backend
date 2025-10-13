package org.codeit.roomunion.common.config.S3;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AmazonProperties.class)
public class AwsConfig {

    private final AmazonProperties amazonProperties;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }
    
    @Bean
    public S3Client s3Client(AwsCredentialsProvider provider) {
        return S3Client.builder()
            .region(Region.of(amazonProperties.getRegion().getStaticRegion()))
            .credentialsProvider(provider)
            .build();
    }
}
