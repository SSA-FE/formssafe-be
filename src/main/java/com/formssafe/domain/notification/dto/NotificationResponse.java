package com.formssafe.domain.notification.dto;

public final class NotificationResponse {

    private NotificationResponse() {
    }

    public record UnreadNotificationCountResponseDto(int count) {
    }

    public record NotificationResponseDto(Long id,
                                          String type,
                                          String content,
                                          boolean isRead,
                                          String createDate) {
    }
}
