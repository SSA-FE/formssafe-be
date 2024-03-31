package com.formssafe.domain.batch.form.service;

import com.formssafe.domain.batch.form.entity.FormBatchEnd;
import com.formssafe.domain.batch.form.entity.FormBatchStart;
import com.formssafe.domain.batch.form.repository.FormBatchEndRepository;
import com.formssafe.domain.batch.form.repository.FormBatchStartRepository;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormBatchService {
    private final FormBatchStartRepository formBatchStartRepository;
    private final FormBatchEndRepository formBatchEndRepository;

    @Transactional
    public void startForm(LocalDateTime now) {
        now = now.withSecond(0).withNano(0);
        List<FormBatchStart> formBatchStartList = formBatchStartRepository.findByServiceTime(now);
        log.info("Auto start forms start: {}", formBatchStartList.stream()
                .map(FormBatchStart::getForm)
                .map(Form::getId).toList());

        for (FormBatchStart formBatchStart : formBatchStartList) {
            Form startForm = formBatchStart.getForm();
            startForm.changeStatus(FormStatus.PROGRESS);
        }

        log.info("Auto start forms end.");
    }

    @Transactional
    public void endForm(LocalDateTime now) {
        now = now.withSecond(0).withNano(0);
        List<FormBatchEnd> formBatchEndList = formBatchEndRepository.findByServiceTime(now);
        log.info("Auto end forms start: {}", formBatchEndList.stream()
                .map(FormBatchEnd::getForm)
                .map(Form::getId).toList());

        for (FormBatchEnd formBatchEnd : formBatchEndList) {
            Form startForm = formBatchEnd.getForm();
            startForm.changeStatus(FormStatus.DONE);
        }

        log.info("Auto end forms end.");
    }
}
