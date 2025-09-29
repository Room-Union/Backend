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
        private final String meeting;

        public Path(String profile, String meeting) {
            this.profile = profile;
            this.meeting = meeting;
=======
        private final String crew;

        public Path(String profile, String crew) {
            this.profile = profile;
            this.crew = crew;
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
        }

    }
}
