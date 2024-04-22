package com.formssafe.domain.form.service;

import com.formssafe.domain.form.entity.Form;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormDoneService {
    private final FormValidateService formValidateService;
    private final FormReadService formReadService;

    @Transactional
    public Form execute(Long formId, Long loginUserId) {
        log.debug("Form Done: id: {}, loginUser: {}", formId, loginUserId);

        Form form = formReadService.findForm(formId);
        formValidateService.validAuthor(form, loginUserId);
        formValidateService.validFormProgress(form);

        form.finishManually(LocalDateTime.now());

        return form;
    }
}
