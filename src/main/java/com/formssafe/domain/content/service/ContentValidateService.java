package com.formssafe.domain.content.service;

import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ContentValidateService {
    public void validDecoration(Decoration decoration) {
        if (decoration.getDetail().length() > 1000) {
            throw new BadRequestException(ErrorCode.CONTENT_DETAIL_OVER_LIMIT, "Decoration의 설명은 1000자 이하여야 합니다.");
        }
    }

    public void validObjectiveQuestion(ObjectiveQuestion objectiveQuestion) {
        if (objectiveQuestion.getTitle().length() < 0 || objectiveQuestion.getTitle().length() > 200) {
            throw new BadRequestException(ErrorCode.CONTENT_TITLE_OVER_LIMIT, "객관식 문항의 제목은 1자 이상 200자 이하여야 합니다.");
        }
        if (objectiveQuestion.getDetail().length() > 1000) {
            throw new BadRequestException(ErrorCode.CONTENT_DETAIL_OVER_LIMIT, "객관식 문항의 설명은 1000자 이하여야 합니다.");
        }
    }

    public void validDescriptiveQuestion(DescriptiveQuestion descriptiveQuestion) {
        if (descriptiveQuestion.getTitle().length() < 0 || descriptiveQuestion.getTitle().length() > 200) {
            throw new BadRequestException(ErrorCode.CONTENT_TITLE_OVER_LIMIT, "객관식 문항의 제목은 1자 이상 200자 이하여야 합니다.");
        }
        if (descriptiveQuestion.getDetail().length() > 1000) {
            throw new BadRequestException(ErrorCode.CONTENT_DETAIL_OVER_LIMIT, "객관식 문항의 설명은 1000자 이하여야 합니다.");
        }
    }
}
