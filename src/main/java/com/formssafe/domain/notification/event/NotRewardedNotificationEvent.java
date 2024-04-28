package com.formssafe.domain.notification.event;

import com.formssafe.domain.notification.dto.NotificationEventDto.NotRewardedNotificationEventDto;
import org.springframework.context.ApplicationEvent;

public class NotRewardedNotificationEvent extends ApplicationEvent {
    private final NotRewardedNotificationEventDto eventDto;

    public NotRewardedNotificationEvent(NotRewardedNotificationEventDto eventDto,
                                        Object source) {
        super(source);
        this.eventDto = eventDto;
    }

    public NotRewardedNotificationEventDto getEventDto() {
        return eventDto;
    }
}
