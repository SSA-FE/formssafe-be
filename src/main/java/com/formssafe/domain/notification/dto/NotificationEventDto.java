package com.formssafe.domain.notification.dto;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.entity.NotificationType;
import java.util.ArrayList;
import java.util.List;

public final class NotificationEventDto {

    private NotificationEventDto() {
    }

    public record FormClosedNotificationEventDto(List<Form> forms) {

        public List<Notification> toEntityList() {
            List<Notification> result = new ArrayList<>();

            for (Form form : forms) {
                result.add(Notification.builder()
                        .receiver(form.getUser())
                        .contentId(form.getId())
                        .notificationType(NotificationType.FINISH_FORM)
                        .content("'" + form.getTitle() + "'" + " 설문이 마감되었습니다.")
                        .build());
            }

            return result;
        }
    }
}
