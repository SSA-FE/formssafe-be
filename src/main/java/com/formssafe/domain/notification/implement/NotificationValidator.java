package com.formssafe.domain.notification.implement;

import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.ForbiddenException;
import java.util.Objects;

public final class NotificationValidator {

    private NotificationValidator() {
    }

    public static void validReceiver(Notification notification, Long receiverId) {
        if (!Objects.equals(notification.getReceiver().getId(), receiverId)) {
            throw new ForbiddenException(ErrorCode.INVALID_RECEIVER,
                    "Invalid receiver id " + receiverId + " for notification receiver id " + notification.getReceiver()
                            .getId());
        }
    }
}
