package com.formssafe.domain.content.question.service;

import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.repository.ObjectiveQuestionRepository;
import com.formssafe.global.exception.type.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ObjectiveQuestionService {
    private final ObjectiveQuestionRepository objectiveQuestionRepository;

    @Transactional
    public ObjectiveQuestion getObjectiveQuestionByUuid(String id, Long formId) {
        ObjectiveQuestion objectiveQuestion = objectiveQuestionRepository.findByUuidAndFormId(id, formId).orElseThrow(
                () -> new BadRequestException("설문에 존재하지 않는 문항입니다.")
        );
        return objectiveQuestion;
    }
}
