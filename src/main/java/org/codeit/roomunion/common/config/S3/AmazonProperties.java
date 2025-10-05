package org.codeit.roomunion.common.config.S3;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cloud.aws")
public class AmazonProperties {

    private final Credentials credentials;
    private final Region region;

    public AmazonProperties(Credentials credentials, Region region) {
        this.credentials = credentials;
        this.region = region;
    }

    @Getter
<<<<<<< HEAD
<<<<<<< HEAD
    public static class Credentials {
=======
    public static class Credentials{
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
=======
    public static class Credentials {
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
        private final String accessKey;
        private final String secretKey;

        public Credentials(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }
    }

    @Getter
    public static class Region {
        private final String staticRegion;

        public Region(String staticRegion) {
            this.staticRegion = staticRegion;
        }
    }
}
