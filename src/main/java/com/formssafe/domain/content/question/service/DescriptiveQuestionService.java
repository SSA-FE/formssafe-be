package com.formssafe.domain.content.question.service;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.repository.DescriptiveQuestionRepository;
import com.formssafe.global.exception.type.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DescriptiveQuestionService {
    private final DescriptiveQuestionRepository descriptiveQuestionRepository;

    public DescriptiveQuestion getDescriptiveQuestionByUuid(String id, Long formId) {
        DescriptiveQuestion descriptiveQuestion = descriptiveQuestionRepository.findByUuidAndFormId(id, formId)
                .orElseThrow(() -> new BadRequestException("설문에 존재하지 않는 문항입니다.")
                );
        return descriptiveQuestion;
    }
}
