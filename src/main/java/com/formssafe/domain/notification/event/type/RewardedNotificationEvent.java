package com.formssafe.domain.notification.event.type;

import com.formssafe.domain.notification.dto.NotificationEventDto.RewardedNotificationEventDto;
import org.springframework.context.ApplicationEvent;

public class RewardedNotificationEvent extends ApplicationEvent {
    private final RewardedNotificationEventDto eventDto;

    public RewardedNotificationEvent(RewardedNotificationEventDto eventDto,
                                     Object source) {
        super(source);
        this.eventDto = eventDto;
    }

    public RewardedNotificationEventDto getEventDto() {
        return eventDto;
    }
}
