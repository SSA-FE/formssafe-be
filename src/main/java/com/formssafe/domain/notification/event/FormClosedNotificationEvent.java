package com.formssafe.domain.notification.event;

import com.formssafe.domain.notification.dto.NotificationEventDto.FormClosedNotificationEventDto;
import org.springframework.context.ApplicationEvent;

public class FormClosedNotificationEvent extends ApplicationEvent {
    private final FormClosedNotificationEventDto eventDto;

    public FormClosedNotificationEvent(FormClosedNotificationEventDto eventDto,
                                       Object source) {
        super(source);
        this.eventDto = eventDto;
    }

    public FormClosedNotificationEventDto getEventDto() {
        return eventDto;
    }
}
