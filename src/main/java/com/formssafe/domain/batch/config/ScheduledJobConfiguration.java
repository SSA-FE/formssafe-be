package com.formssafe.domain.batch.config;

import com.formssafe.domain.batch.form.service.FormBatchService;
import com.formssafe.domain.form.entity.Form;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledJobConfiguration {
    private final FormBatchService formBatchService;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void scheduledStartForm() {
        List<Form> forms = formBatchService.startForm(LocalDateTime.now());
        formBatchService.registerEndForm(forms);
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void scheduledEndForm() {
        formBatchService.endForm(LocalDateTime.now());
    }
}
