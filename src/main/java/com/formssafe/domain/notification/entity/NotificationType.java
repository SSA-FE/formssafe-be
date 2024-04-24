package com.formssafe.domain.notification.entity;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum NotificationType {
    SUBSCRIBED_FORM("subscribed_form"),
    FINISH_FORM("finish_form"),
    GET_REWARD("get_reward"),
    RESPOND_FORM("respond_form");

    private static final Map<String, NotificationType> convertor = Arrays.stream(
                    NotificationType.values())
            .collect(Collectors.toMap(NotificationType::getDisplayName, Function.identity()));
    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean exists(String displayName) {
        return convertor.containsKey(displayName);
    }

    public Optional<NotificationType> from(String displayName) {
        return Optional.ofNullable(convertor.get(displayName));
    }
}
