package com.formssafe.domain.notification.implement;

import com.formssafe.domain.notification.dto.NotificationParam.NotificationSearchDto;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.repository.NotificationRepository;
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

    public int getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findAllByReceiverIdAndIsReadFalse(userId);
    }

    public List<Notification> getNotifications(Long userId,
                                               NotificationSearchDto searchDto) {
        return notificationRepository.findAllByReceiverIdAndIdAfter(userId, searchDto.cursor());
    }
}
