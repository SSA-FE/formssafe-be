package com.formssafe.domain.notification.dto;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.entity.NotificationType;
import com.formssafe.domain.user.entity.User;
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

    public record RewardedNotificationEventDto(List<Form> forms, List<List<User>> rewardedUsers) {

        public List<Notification> toEntityList() {
            List<Notification> result = new ArrayList<>();

            for (int i = 0; i < forms.size(); ++i) {
                Form form = forms.get(i);
                List<User> users = rewardedUsers.get(i);
                for (User user : users) {
                    result.add(Notification.builder()
                            .receiver(user)
                            .contentId(form.getId())
                            .notificationType(NotificationType.GET_REWARD)
                            .content("'" + form.getTitle() + "'" + " 설문의 경품에 당첨되었습니다!")
                            .build());
                }
            }

            return result;
        }
    }

    public record NotRewardedNotificationEventDto(List<Form> forms, List<List<User>> notRewardedUsers) {

        public List<Notification> toEntityList() {
            List<Notification> result = new ArrayList<>();

            for (int i = 0; i < forms.size(); ++i) {
                Form form = forms.get(i);
                List<User> users = notRewardedUsers.get(i);
                for (User user : users) {
                    result.add(Notification.builder()
                            .receiver(user)
                            .contentId(form.getId())
                            .notificationType(NotificationType.GET_REWARD)
                            .content("'" + form.getTitle() + "'" + " 설문의 경품에 당첨되지 않았습니다. 다음 기회에 도전해주세요!")
                            .build());
                }
            }

            return result;
        }
    }
}
