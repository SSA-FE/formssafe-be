package com.formssafe.domain.form.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormCommonService {
    private final FormRepository formRepository;

    public Form findForm(Long formId) {
        return formRepository.findById(formId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.FORM_NOT_FOUND,
                        "Form doesn't exist for id " + formId));
    }
}
