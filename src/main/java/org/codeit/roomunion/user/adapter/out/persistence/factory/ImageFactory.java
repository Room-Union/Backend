package org.codeit.roomunion.user.adapter.out.persistence.factory;

import org.codeit.roomunion.common.config.S3.S3Properties;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.AppointmentEntity;
import org.codeit.roomunion.meeting.domain.model.Appointment;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class ImageFactory {

    private static final String IMAGE_PATH_FORMAT = "https://%s.s3.ap-northeast-2.amazonaws.com/%s";
    private static final String EMPTY_STRING = "";

    private final S3Properties s3Properties;

    public ImageFactory(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
    }

    public String createProfileImagePath(UserEntity userEntity) {
        if (!userEntity.isHasImage()) {
            return EMPTY_STRING;
        }
        String profileImagePath = User.PROFILE_IMAGE_PATH.formatted(userEntity.getId());
        return IMAGE_PATH_FORMAT.formatted(s3Properties.getBucket(), profileImagePath);
    }

    public String createAppointmentImagePath(AppointmentEntity appointmentEntity) {
        if (!appointmentEntity.getHasImage()) {
            return EMPTY_STRING;
        }
        String appointmentImagePath = Appointment.PROFILE_IMAGE_PATH.formatted(appointmentEntity.getId());
        return IMAGE_PATH_FORMAT.formatted(s3Properties.getBucket(), appointmentImagePath);
    }

}
