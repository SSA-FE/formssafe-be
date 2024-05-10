package com.formssafe.domain.content.question.service;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.repository.DescriptiveQuestionRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DescriptiveQuestionService {
    private final DescriptiveQuestionRepository descriptiveQuestionRepository;

    public DescriptiveQuestion getDescriptiveQuestionByUuid(String id, Long formId) {
        DescriptiveQuestion descriptiveQuestion = descriptiveQuestionRepository.findByUuidAndFormId(id, formId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.DESCRIPTIVE_QUESTION_NOT_EXIST, "설문에 존재하지 않는 문항입니다.")
                );
        return descriptiveQuestion;
    }

    public List<Long> getDescriptiveQuestionIdByDisposalTime(LocalDateTime now) {
        return descriptiveQuestionRepository.findIdByDisposalTime(now);
    }
}
