package com.formssafe.domain.notification.event;

import com.formssafe.domain.notification.dto.NotificationEventDto.FormClosedNotificationEventDto;
import com.formssafe.domain.notification.entity.Notification;
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

    @TransactionalEventListener(value = FormClosedNotificationEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyFormClosed(FormClosedNotificationEvent event) {
        FormClosedNotificationEventDto eventDto = event.getEventDto();

        List<Notification> notifications = eventDto.toEntityList();
        notificationRepository.saveAll(notifications);
    }
}
