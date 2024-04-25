package com.formssafe.domain.notification.repository;

import com.formssafe.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    int countByReceiverIdAndIsReadFalse(Long userId);

    List<Notification> findAllByReceiverIdAndIsReadFalse(Long userId);
}
