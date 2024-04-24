package com.formssafe.domain.notification.dto;

public final class NotificationParam {

    private NotificationParam() {
    }

    public record NotificationSearchDto(Long cursor) {
    }
}
