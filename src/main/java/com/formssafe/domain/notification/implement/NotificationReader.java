package com.formssafe.domain.notification.implement;

import com.formssafe.domain.notification.dto.NotificationParam.NotificationSearchDto;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.repository.NotificationRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.DataNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationReader {
    private final NotificationRepository notificationRepository;

    public Notification findNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND,
                        "Notification not found for id " + notificationId));
    }

    public int findUnreadNotificationCount(Long userId) {
        return notificationRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    public List<Notification> findUnreadNotifications(Long userId,
                                                      NotificationSearchDto searchDto) {
        Long top = searchDto.top();
        if (top == null) {
            top = Long.MAX_VALUE;
        }

        return notificationRepository.findTop10ByReceiverIdAndIsReadFalseAndIdBeforeOrderByIdDesc(userId, top);
    }

    public List<Notification> findNotifications(Long userId,
                                                NotificationSearchDto searchDto) {
        Long top = searchDto.top();
        if (top == null) {
            top = Long.MAX_VALUE;
        }

        return notificationRepository.findTop10ByReceiverIdAndIdBeforeOrderByIdDesc(userId, top);
    }
}
