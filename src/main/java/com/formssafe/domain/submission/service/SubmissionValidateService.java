package com.formssafe.domain.submission.service;

import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class SubmissionValidateService {
    public void validDescriptiveSubmission(DescriptiveSubmission descriptiveSubmission,
                                           DescriptiveQuestionType descriptiveQuestionType) {
        if (descriptiveQuestionType.displayName().equals("short")) {
            if (descriptiveSubmission.getContent().length() > 500) {
                throw new BadRequestException(ErrorCode.SHORT_QUESTION_SUBMISSION_CONTENT_OVER_LIMIT,
                        "short형 질문의 응답은 500자 이내여야 합니다. : " + descriptiveSubmission.getContent());
            }
        } else if (descriptiveQuestionType.displayName().equals("long")) {
            if (descriptiveSubmission.getContent().length() > 5000) {
                throw new BadRequestException(ErrorCode.LONG_QUESTION_SUBMISSION_CONTENT_OVER_LIMIT,
                        "long형 질문의 응답은 5000자 이내여야 합니다. : " + descriptiveSubmission.getContent());
            }
        }
    }
}
