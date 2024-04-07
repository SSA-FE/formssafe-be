package com.formssafe.domain.content.question.service;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.repository.DescriptiveQuestionRepository;
import com.formssafe.domain.content.question.repository.ObjectiveQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public ObjectiveQuestion getObjectiveQuestionByUuid(String id){
        //TODO : 오류처리 해야함
        ObjectiveQuestion objectiveQuestion = objectiveQuestionRepository.findByUuid(id).orElseThrow(
                () -> new EntityNotFoundException(id)
        );
        return objectiveQuestion;
    }
}
