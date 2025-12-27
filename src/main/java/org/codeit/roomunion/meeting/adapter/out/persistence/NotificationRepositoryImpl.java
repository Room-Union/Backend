package org.codeit.roomunion.meeting.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.NotificationEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.NotificationJpaRepository;
import org.codeit.roomunion.meeting.application.port.out.notification.NotificationRepository;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.codeit.roomunion.meeting.domain.model.NotificationType;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Notification save(Long userId, NotificationType type, String message, String targetUrl, String imageUrl) {
        UserEntity user = userJpaRepository.findById(userId)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        NotificationEntity saved = notificationJpaRepository.save(NotificationEntity.from(user, type, message, targetUrl, imageUrl));
        return saved.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> findUnread(Long userId) {
        return notificationJpaRepository.findUnread(userId).stream()
            .map(NotificationEntity::toDomain)
            .toList();
    }

    @Override
    public void markReadAll(Long userId, LocalDateTime readAt) {
        notificationJpaRepository.markReadAll(userId, readAt);
    }

    @Override
    public void markReadByIds(Long userId, List<Long> ids, LocalDateTime readAt) {
        notificationJpaRepository.markReadByIds(userId, ids, readAt);
    }
}
