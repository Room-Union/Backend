package org.codeit.roomunion;

import org.codeit.roomunion.common.config.S3.AmazonProperties;
import org.codeit.roomunion.common.config.S3.S3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AmazonProperties.class, S3Properties.class})
public class RoomunionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomunionApplication.class, args);
    }

}
