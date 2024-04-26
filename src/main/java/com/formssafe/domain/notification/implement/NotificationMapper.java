package com.formssafe.domain.notification.implement;

import com.formssafe.domain.notification.dto.NotificationResponse.NotificationResponseDto;
import com.formssafe.domain.notification.dto.NotificationResponse.UnreadNotificationCountResponseDto;
import com.formssafe.domain.notification.entity.Notification;
import java.util.List;

public final class NotificationMapper {

    private NotificationMapper() {
    }

    public static UnreadNotificationCountResponseDto createUnreadNotificationCountResponseDto(int unreadCount) {
        return new UnreadNotificationCountResponseDto(unreadCount);
    }

    public static List<NotificationResponseDto> createNotificationResponseDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationResponseDto::from)
                .sorted((n1, n2) -> n2.createDate().compareTo(n1.createDate()))
                .toList();
    }
}
