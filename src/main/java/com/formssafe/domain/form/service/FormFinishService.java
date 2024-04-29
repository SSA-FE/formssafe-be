package com.formssafe.domain.form.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.notification.dto.NotificationEventDto.FormClosedNotificationEventDto;
import com.formssafe.domain.notification.event.type.FormClosedNotificationEvent;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormFinishService {
    private final FormValidateService formValidateService;
    private final FormReadService formReadService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Form execute(Long formId, Long loginUserId) {
        log.debug("Form Done: id: {}, loginUser: {}", formId, loginUserId);

        Form form = formReadService.findForm(formId);
        formValidateService.validAuthor(form, loginUserId);
        formValidateService.validFormProgress(form);

        form.finishManually(LocalDateTime.now());
        applicationEventPublisher.publishEvent(new FormClosedNotificationEvent(
                new FormClosedNotificationEventDto(List.of(form)),
                this));

        return form;
    }
}
