package com.formssafe.domain.notification.event;

import com.formssafe.domain.notification.dto.NotificationEventDto.FormClosedNotificationEventDto;
import com.formssafe.domain.notification.dto.NotificationEventDto.FormParticipantsNotificationEventDto;
import com.formssafe.domain.notification.dto.NotificationEventDto.NotRewardedNotificationEventDto;
import com.formssafe.domain.notification.dto.NotificationEventDto.RewardCategoryRegistNotificationEventDto;
import com.formssafe.domain.notification.dto.NotificationEventDto.RewardedNotificationEventDto;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.event.type.FormClosedNotificationEvent;
import com.formssafe.domain.notification.event.type.FormParticipantsNotificationEvent;
import com.formssafe.domain.notification.event.type.NotRewardedNotificationEvent;
import com.formssafe.domain.notification.event.type.RewardCategoryRegistNotificationEvent;
import com.formssafe.domain.notification.event.type.RewardedNotificationEvent;
import com.formssafe.domain.notification.repository.NotificationRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class NotificationEventListener {
    private final NotificationRepository notificationRepository;

    public NotificationEventListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @TransactionalEventListener(value = FormParticipantsNotificationEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyFormParticipate(FormParticipantsNotificationEvent event) {
        FormParticipantsNotificationEventDto eventDto = event.getEventDto();

        Notification notification = eventDto.toEntity();
        notificationRepository.save(notification);
    }

    @TransactionalEventListener(value = RewardCategoryRegistNotificationEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyRewardCategoryFormRegist(RewardCategoryRegistNotificationEvent event) {
        RewardCategoryRegistNotificationEventDto eventDto = event.getEventDto();

        List<Notification> notifications = eventDto.toEntityList();
        notificationRepository.saveAll(notifications);
    }

    @TransactionalEventListener(value = FormClosedNotificationEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyFormClosed(FormClosedNotificationEvent event) {
        FormClosedNotificationEventDto eventDto = event.getEventDto();

        List<Notification> notifications = eventDto.toEntityList();
        notificationRepository.saveAll(notifications);
    }

    @TransactionalEventListener(value = RewardedNotificationEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyRewarded(RewardedNotificationEvent event) {
        RewardedNotificationEventDto eventDto = event.getEventDto();

        List<Notification> notifications = eventDto.toEntityList();
        notificationRepository.saveAll(notifications);
    }

    @TransactionalEventListener(value = NotRewardedNotificationEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyNotRewarded(NotRewardedNotificationEvent event) {
        NotRewardedNotificationEventDto eventDto = event.getEventDto();

        List<Notification> notifications = eventDto.toEntityList();
        notificationRepository.saveAll(notifications);
    }
}
