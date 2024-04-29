package com.formssafe.domain.notification.event.type;

import com.formssafe.domain.notification.dto.NotificationEventDto.RewardCategoryRegistNotificationEventDto;
import org.springframework.context.ApplicationEvent;

public class RewardCategoryRegistNotificationEvent extends ApplicationEvent {
    private final RewardCategoryRegistNotificationEventDto eventDto;

    public RewardCategoryRegistNotificationEvent(RewardCategoryRegistNotificationEventDto eventDto, Object source) {
        super(source);
        this.eventDto = eventDto;
    }

    public RewardCategoryRegistNotificationEventDto getEventDto() {
        return eventDto;
    }
}
