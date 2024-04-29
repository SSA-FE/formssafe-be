package com.formssafe.domain.notification.dto;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.entity.NotificationType;
import com.formssafe.domain.subscribe.entity.Subscribe;
import com.formssafe.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;

public final class NotificationEventDto {

    private NotificationEventDto() {
    }

    public record FormParticipantsNotificationEventDto(Form form, User user) {
        public Notification toEntity() {
            return Notification.builder()
                    .receiver(form.getUser())
                    .contentId(form.getId())
                    .notificationType(NotificationType.RESPOND_FORM)
                    .content(user.getNickname() + "님이 '" + form.getTitle() + "'" + "에 대한 새로운 응답을 작성했습니다.")
                    .build();
        }
    }

    public record RewardCategoryRegistNotificationEventDto(Form form, List<Subscribe> subscribeList) {
        public List<Notification> toEntityList() {
            List<Notification> result = new ArrayList<>();

            for (Subscribe subscribe : subscribeList) {
                result.add(Notification.builder()
                        .receiver(subscribe.getUser())
                        .contentId(form.getId())
                        .notificationType(NotificationType.SUBSCRIBED_FORM)
                        .content("구독하신 카테고리 " + "'" + subscribe.getRewardCategory().getRewardCategoryName() + "'"
                                + "의 새로운 설문조사가 등록되었습니다.")
                        .build()
                );
            }
            return result;
        }
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
