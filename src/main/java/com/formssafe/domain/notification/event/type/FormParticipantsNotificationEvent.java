package com.formssafe.domain.notification.event.type;

import com.formssafe.domain.notification.dto.NotificationEventDto.FormParticipantsNotificationEventDto;
import org.springframework.context.ApplicationEvent;

public class FormParticipantsNotificationEvent extends ApplicationEvent {
    private final FormParticipantsNotificationEventDto eventDto;

    public FormParticipantsNotificationEvent(FormParticipantsNotificationEventDto eventDto,
                                             Object source) {
        super(source);
        this.eventDto = eventDto;
    }

    public FormParticipantsNotificationEventDto getEventDto() {
        return eventDto;
    }
}
