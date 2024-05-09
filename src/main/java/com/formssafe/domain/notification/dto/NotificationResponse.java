package com.formssafe.domain.notification.dto;

import com.formssafe.domain.notification.entity.Notification;
import java.util.List;

public final class NotificationResponse {

    private NotificationResponse() {
    }

    public record UnreadNotificationCountResponseDto(int count) {
    }

    public record NotificationResponseDto(Long id,
                                          String type,
                                          String content,
                                          Long contentId,
                                          boolean isRead,
                                          String createDate) {

        public static NotificationResponseDto from(Notification notification) {
            return new NotificationResponseDto(notification.getId(),
                    notification.getNotificationType().name(),
                    notification.getContent(),
                    notification.getContentId(),
                    notification.isRead(),
                    notification.getCreateDate().toString());
        }
    }

    public record NotificationCursor(Long top) {
    }

    public record NotificationListResponseDto(List<NotificationResponseDto> notifications,
                                              NotificationCursor cursor) {

    }
}
