package com.formssafe.domain.batch.form.service;

import com.formssafe.domain.batch.form.entity.FormBatchEnd;
import com.formssafe.domain.batch.form.entity.FormBatchStart;
import com.formssafe.domain.batch.form.repository.FormBatchEndRepository;
import com.formssafe.domain.batch.form.repository.FormBatchStartRepository;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<Form> startForm(LocalDateTime now) {
        log.info("Auto start forms start");

        now = now.withSecond(0).withNano(0);
        List<FormBatchStart> formBatchStartList = formBatchStartRepository.findByServiceTime(now);
        log.info("start forms: {}", formBatchStartList.stream()
                .map(FormBatchStart::getForm)
                .map(Form::getId).toList());

        List<Form> startedForms = new ArrayList<>();
        for (FormBatchStart formBatchStart : formBatchStartList) {
            Form startForm = formBatchStart.getForm();
            startForm.changeStatus(FormStatus.PROGRESS);
            startedForms.add(startForm);
        }
        log.info("Auto start forms end.");

        return startedForms;
    }

    @Transactional
    public void registerStartFormManually(Form form) {
        FormBatchStart formBatchStart = FormBatchStart.builder()
                .form(form)
                .serviceTime(form.getStartDate())
                .build();
        formBatchStartRepository.save(formBatchStart);
    }

    @Transactional
    public void registerEndFormManually(Form form) {
        FormBatchEnd formBatchEnd = FormBatchEnd.builder()
                .form(form)
                .serviceTime(form.getEndDate())
                .build();
        formBatchEndRepository.save(formBatchEnd);
    }

    @Transactional
    public void registerEndForm(List<Form> startedForms) {
        log.info("Register form to form end batch table: {}", startedForms.stream()
                .map(Form::getId).toList());

        List<FormBatchEnd> endForms = startedForms.stream()
                .map(form -> FormBatchEnd.builder()
                        .serviceTime(form.getEndDate())
                        .form(form)
                        .build())
                .toList();

        formBatchEndRepository.saveAll(endForms);
        log.info("Register form to form end batch table end");
    }

    @Transactional
    public void endForm(LocalDateTime now) {
        log.info("Auto end forms start");

        now = now.withSecond(0).withNano(0);
        List<FormBatchEnd> formBatchEndList = formBatchEndRepository.findByServiceTime(now);
        log.info("end forms: {}", formBatchEndList.stream()
                .map(FormBatchEnd::getForm)
                .map(Form::getId).toList());

        for (FormBatchEnd formBatchEnd : formBatchEndList) {
            Form startForm = formBatchEnd.getForm();
            startForm.changeStatus(FormStatus.DONE);
        }

        log.info("Auto end forms end.");
    }
}
