package com.formssafe.domain.batch.config;

import com.formssafe.domain.batch.form.service.FormBatchService;
import com.formssafe.domain.submission.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledJobConfiguration {
    private final FormBatchService formBatchService;
    private final SubmissionService submissionService;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void scheduledEndForm() {
        formBatchService.endForm(LocalDateTime.now());
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void scheduledDisposalPrivacy() {
        submissionService.disposalPrivacy(LocalDateTime.now());
    }

    @Scheduled(cron = "0 * * * * *")
    public void scheduledTop10FormSave() {
        formBatchService.saveTop10HotForm(LocalDateTime.now());
    }
}
