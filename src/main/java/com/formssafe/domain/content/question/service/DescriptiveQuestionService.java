package com.formssafe.domain.content.question.service;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.repository.DescriptiveQuestionRepository;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.global.exception.type.DataNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DescriptiveQuestionService {
    private final DescriptiveQuestionRepository descriptiveQuestionRepository;

    @Transactional
    public DescriptiveQuestion getDescriptiveQuestionByUuid(String id){
        //TODO : 오류처리 해야함
        DescriptiveQuestion descriptiveQuestion = descriptiveQuestionRepository.findByUuid(id).orElseThrow(
            ()->new EntityNotFoundException(id));
        return descriptiveQuestion;
    }
}
