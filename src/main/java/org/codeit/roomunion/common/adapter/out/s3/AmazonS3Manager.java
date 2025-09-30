package org.codeit.roomunion.common.adapter.out.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
=======
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.adapter.out.persistence.jpa.UuidJpaRepository;
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
import org.codeit.roomunion.common.config.S3.S3Properties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    private final S3Properties s3Properties;
<<<<<<< HEAD
    
    public String uploadFile(String keyName, MultipartFile file) {
=======

    private final UuidJpaRepository uuidJpaRepository;

    public String uploadFile(String keyName, MultipartFile file){
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(s3Properties.getBucket(), keyName, file.getInputStream(), metadata));
<<<<<<< HEAD
        } catch (IOException e) {
=======
        }catch (IOException e){
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(s3Properties.getBucket(), keyName).toString();
    }
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======

    public String profileImageKey(UuidEntity uuidEntity) {
        return s3Properties.getPath().getProfile() + "/" + uuidEntity.getUuid();
    }

    public String crewImageKey(UuidEntity uuidEntity) {
        return s3Properties.getPath().getCrew() + '/' + uuidEntity.getUuid();
    }
>>>>>>> c9ee85d (:recycle: AmazonS3Manager에 S3 Path 복구 (#7))
    

>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
}
