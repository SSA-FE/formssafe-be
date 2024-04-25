package com.formssafe.domain.notification.repository;

import com.formssafe.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    int countByReceiverIdAndIsReadFalse(Long userId);

    List<Notification> findAllByReceiverIdAndIsReadFalse(Long userId);

    List<Notification> findAllByReceiverIdAndIdAfter(Long userId, Long notificationId);

    @Modifying
    @Query("update Notification n set n.isRead = true where n.receiver.id = :userId")
    void markAllAsReadByReceiverId(@Param("userId") Long userId);
}
