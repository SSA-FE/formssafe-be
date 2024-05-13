package com.formssafe.domain.notification.implement;

import com.formssafe.domain.notification.dto.NotificationResponse.NotificationCursor;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationListResponseDto;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationResponseDto;
import com.formssafe.domain.notification.dto.NotificationResponse.UnreadNotificationCountResponseDto;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.page.PageImpl;
import java.util.List;

public final class NotificationMapper {

    private NotificationMapper() {
    }

    public static UnreadNotificationCountResponseDto createUnreadNotificationCountResponseDto(int unreadCount) {
        return new UnreadNotificationCountResponseDto(unreadCount);
    }

    public static NotificationListResponseDto createNotificationListResponseDto(PageImpl<Notification> page) {
        List<NotificationResponseDto> notificationResponses = page.getContents().stream()
                .map(NotificationResponseDto::from)
                .toList();

        NotificationCursor cursor = getNotificationCursor(page);
        return new NotificationListResponseDto(notificationResponses, cursor);
    }

    private static NotificationCursor getNotificationCursor(PageImpl<Notification> page) {
        if (page.isLast()) {
            return new NotificationCursor(-1L);
        }
        Notification lastNotification = page.getContents().get(page.getSize() - 1);
        return new NotificationCursor(lastNotification.getId());
    }
}
