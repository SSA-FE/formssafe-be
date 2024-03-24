package com.formssafe.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formssafe.domain.question.entity.DescriptiveQuestion;
import com.formssafe.domain.question.entity.ObjectiveQuestion;
import com.formssafe.domain.question.entity.ObjectiveQuestionOption;
import com.formssafe.domain.question.entity.Question;
import com.formssafe.global.util.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public final class QuestionResponse {

    public record QuestionDetailDto(@Schema(description = "설문 문항 id") Long id,
                                    @Schema(description = "설문 문항 타입") String type,
                                    @Schema(description = "설문 문항 질문") String title,
                                    @Schema(description = "설문 문항 설명") String description,
                                    @Schema(description = "객관식 문항일 시, 보기 목록")
                                    @JsonInclude(JsonInclude.Include.NON_NULL)
                                    List<ObjectiveQuestionOption> options,
                                    @Schema(description = "필수 응답 여부") boolean isRequired,
                                    @Schema(description = "개인 정보 포함 응답 여부") boolean isPrivacy) {

        public static QuestionDetailDto from(Question question) {
            if (question instanceof DescriptiveQuestion dq) {
                return fromDescriptiveQuestion(dq);
            } else if (question instanceof ObjectiveQuestion oq) {
                return fromObjectiveQuestion(oq);
            }
            return null;
        }

        private static QuestionDetailDto fromDescriptiveQuestion(DescriptiveQuestion question) {
            return new QuestionDetailDto(question.getId(),
                    question.getQuestionType().displayName(),
                    question.getTitle(),
                    question.getDetail(),
                    null,
                    question.isRequired(),
                    question.isPrivacy());
        }

        private static QuestionDetailDto fromObjectiveQuestion(ObjectiveQuestion question) {
            return new QuestionDetailDto(question.getId(),
                    question.getQuestionType().displayName(),
                    question.getTitle(),
                    question.getDetail(),
                    JsonConverter.toList(question.getQuestionOption(), ObjectiveQuestionOption.class),
                    question.isRequired(),
                    question.isPrivacy());
        }
    }
}

