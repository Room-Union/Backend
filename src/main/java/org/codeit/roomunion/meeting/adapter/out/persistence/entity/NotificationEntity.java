package org.codeit.roomunion.meeting.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.codeit.roomunion.meeting.domain.model.NotificationType;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;

@Entity
@Table(name = "notification")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "target_url")
    private String targetUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public static NotificationEntity from(UserEntity user, NotificationType type, String message, String targetUrl, String imageUrl) {
        return NotificationEntity.builder()
            .user(user)
            .type(type)
            .message(message)
            .targetUrl(targetUrl)
            .imageUrl(imageUrl)
            .build();
    }

    public Notification toDomain() {
        return Notification.of(
            this.getId(),
            this.getUser().getId(),
            this.getType(),
            this.getMessage(),
            this.getTargetUrl(),
            this.getImageUrl(),
            this.isRead(),
            this.getCreatedAt(),
            this.getReadAt()
        );
    }



}
