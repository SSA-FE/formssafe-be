package com.formssafe.domain.content.service;

import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.decoration.repository.DecorationRepository;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionType;
import com.formssafe.domain.content.question.repository.DescriptiveQuestionRepository;
import com.formssafe.domain.content.question.repository.ObjectiveQuestionRepository;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.global.exception.type.BadRequestException;
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
public class ContentService {
    private final ObjectiveQuestionRepository objectiveQuestionRepository;
    private final DescriptiveQuestionRepository descriptiveQuestionRepository;
    private final DecorationRepository decorationRepository;

    @Transactional
    public void createContents(List<ContentCreateDto> contentCreateDtos, Form form) {
        List<ObjectiveQuestion> objectiveQuestions = new ArrayList<>();
        List<DescriptiveQuestion> descriptiveQuestions = new ArrayList<>();
        List<Decoration> decorations = new ArrayList<>();

        int position = 1;
        for (ContentCreateDto q : contentCreateDtos) {
            if (DecorationType.exists(q.type())) {
                decorations.add(q.toDecoration(form, position));
            } else if (ObjectiveQuestionType.exists(q.type())) {
                objectiveQuestions.add(q.toObjectiveQuestion(form, position));
            } else if (DescriptiveQuestionType.exists(q.type())) {
                descriptiveQuestions.add(q.toDescriptiveQuestion(form, position));
            } else {
                throw new BadRequestException("유효하지 않은 옵션입니다.: " + q.type());
            }
            ++position;
        }

        objectiveQuestionRepository.saveAll(objectiveQuestions);
        descriptiveQuestionRepository.saveAll(descriptiveQuestions);
        decorationRepository.saveAll(decorations);
    }

    @Transactional
    public void deleteContents(Form form) {
        decorationRepository.deleteAllByForm(form);
        objectiveQuestionRepository.deleteAllByForm(form);
        descriptiveQuestionRepository.deleteAllByForm(form);
    }
}
