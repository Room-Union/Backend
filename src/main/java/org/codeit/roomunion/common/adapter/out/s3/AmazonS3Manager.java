package org.codeit.roomunion.common.adapter.out.s3;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.common.config.S3.S3Properties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final S3Client s3Client;

    private final S3Properties s3Properties;

    public String uploadFile(String keyName, MultipartFile file) {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(keyName)
                .contentType(file.getContentType())
                .build();

            s3Client.putObject(putRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String url = s3Client.utilities()
                .getUrl(GetUrlRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(keyName)
                    .build())
                .toExternalForm();

            return url;
        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile", e);
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }
    }
}
