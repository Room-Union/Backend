package org.codeit.roomunion.common.config.S3;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3Properties {

    private final String bucket;
    private final Path path;

    public S3Properties(String bucket, Path path) {
        this.bucket = bucket;
        this.path = path;
    }

    @Getter
    public static class Path {
        private final String profile;
<<<<<<< HEAD
<<<<<<< HEAD
        private final String meeting;

        public Path(String profile, String meeting) {
            this.profile = profile;
            this.meeting = meeting;
=======
        private final String crew;
=======
        private final String meeting;
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))

        public Path(String profile, String meeting) {
            this.profile = profile;
<<<<<<< HEAD
            this.crew = crew;
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
=======
            this.meeting = meeting;
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
        }

    }
}
