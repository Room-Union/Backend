package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("""
              select n from NotificationEntity n
              where n.user.id = :userId and n.isRead = false
              order by n.createdAt desc
        """)
    List<NotificationEntity> findUnread(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
              update NotificationEntity n
              set n.isRead = true, n.readAt = :readAt
              where n.user.id = :userId and n.isRead = false
        """)
    int markReadAll(Long userId, LocalDateTime readAt);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update NotificationEntity n
            set n.isRead = true, n.readAt = :readAt
            where n.user.id = :userId and n.id in :ids
        """)
    int markReadByIds(Long userId, List<Long> ids, LocalDateTime readAt);

}
