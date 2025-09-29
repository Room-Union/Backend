package org.codeit.roomunion.common.config.S3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
=======
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmazonConfig {

    private final AmazonProperties amazonProperties;

    @Bean
    public AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(
            amazonProperties.getCredentials().getAccessKey(),
            amazonProperties.getCredentials().getSecretKey()
        );
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(AWSCredentials credentials) {
        return new AWSStaticCredentialsProvider(credentials);
    }

    @Bean
    public AmazonS3 amazonS3(AWSCredentialsProvider provider) {
        return AmazonS3ClientBuilder.standard()
            .withRegion(amazonProperties.getRegion().getStaticRegion())
            .withCredentials(provider)
            .build();
    }
}
