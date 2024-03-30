package com.formssafe.domain.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledJobConfiguration {

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void scheduledStartForm() {
        log.info("scheduled!!");
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void scheduledEndForm() {
        log.info("scheduled!!");
    }
}
