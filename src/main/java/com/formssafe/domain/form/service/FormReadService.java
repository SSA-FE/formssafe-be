package com.formssafe.domain.form.service;

import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.content.question.dto.ContentConverter;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.DataNotFoundException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormReadService {
    private final FormRepository formRepository;
    private final ContentConverter contentConverter;

    public Form findForm(Long formId) {
        return formRepository.findById(formId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.FORM_NOT_FOUND,
                        "Form doesn't exist for id " + formId));
    }

    public Form findFormWithUserAndTag(Long formId) {
        return formRepository.findFormWithUserAndTag(formId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.FORM_NOT_FOUND,
                        "Form doesn't exist for id " + formId));
    }

    public List<Form> findFormListWithFilter(SearchDto searchDto) {
        return formRepository.findFormWithFiltered(searchDto);
    }

    public List<Content> getContentList(Long formId) {
        return formRepository.findContentsById(formId).stream()
                .map(contentConverter::convert)
                .sorted(Comparator.comparingInt(Content::getPosition))
                .toList();
    }
}
