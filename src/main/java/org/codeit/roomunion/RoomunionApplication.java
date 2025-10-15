package org.codeit.roomunion;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.codeit.roomunion.common.config.RedisProperties;
import org.codeit.roomunion.common.config.S3.AmazonProperties;
import org.codeit.roomunion.common.config.S3.S3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = { S3AutoConfiguration.class })
@EnableConfigurationProperties({AmazonProperties.class, S3Properties.class, RedisProperties.class})
public class RoomunionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomunionApplication.class, args);
    }

}
