package com.formssafe.domain.notification.implement;

import com.formssafe.domain.notification.dto.NotificationResponse.NotificationCursor;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationListResponseDto;
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

    public static NotificationListResponseDto createNotificationListResponseDto(List<Notification> notifications) {
        List<NotificationResponseDto> notificationResponses = notifications.stream()
                .map(NotificationResponseDto::from)
                .toList();

        NotificationResponseDto lastNotification = notificationResponses.get(
                notificationResponses.size() - 1);
        NotificationCursor cursor = new NotificationCursor(lastNotification.id());

        return new NotificationListResponseDto(notificationResponses, cursor);
    }
}
