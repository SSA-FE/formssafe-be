package com.formssafe.domain.batch.form.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.hotform.service.HotFormService;
import com.formssafe.domain.notification.dto.NotificationEventDto.FormClosedNotificationEventDto;
import com.formssafe.domain.notification.event.type.FormClosedNotificationEvent;
import com.formssafe.domain.reward.service.RewardRecipientsSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormBatchService {
    private final FormRepository formRepository;
    private final HotFormService hotFormService;
    private final RewardRecipientsSelectService rewardRecipientsSelectService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void endForm(LocalDateTime now) {
        log.info("Auto end forms start");

        now = now.withSecond(0).withNano(0);
        List<Form> endForms = formRepository.findAllByEndDate(now);
        log.info("end forms: {}", endForms.stream()
                .map(Form::getId).toList());

        for (Form form : endForms) {
            if (!FormStatus.PROGRESS.equals(form.getStatus())) {
                log.warn("form is not progress. id: {} status: {}", form.getId(), form.getStatus());
                continue;
            }
            form.finish();

            if (form.getReward() != null) {
                rewardRecipientsSelectService.execute(form);
            }
        }

        applicationEventPublisher.publishEvent(new FormClosedNotificationEvent(
                new FormClosedNotificationEventDto(endForms),
                this));

        log.info("Auto end forms end.");
    }

    @Transactional
    public void saveTop10HotForm(LocalDateTime now) {
        List<Form> top10HotForms = formRepository.findTop10HotForm();
        now = now.withSecond(0).withNano(0);
        log.info("top10 forms: {}", top10HotForms.stream()
                .map(Form::getId).toList());

        hotFormService.saveHotForm(top10HotForms, now);
    }
}
