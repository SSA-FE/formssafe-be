package com.formssafe.domain.notification.implement;

import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationUpdater {
    private final NotificationRepository notificationRepository;

    public void markAsRead(Notification notification) {
        notification.read();
    }

    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadByReceiverId(userId);
    }
}
