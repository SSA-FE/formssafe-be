package com.formssafe.domain.content.question.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formssafe.domain.content.dto.ContentResponseDto;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionOption;
import com.formssafe.domain.content.question.entity.Question;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.DtoConvertException;
import com.formssafe.global.util.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
public class QuestionResponseDto extends ContentResponseDto {
    @Schema(description = "설문 문항 질문")
    String title;
    @Schema(description = "객관식 문항일 시, 보기 목록")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ObjectiveQuestionOption> options;
    @Schema(description = "필수 응답 여부")
    boolean isRequired;
    @Schema(description = "개인 정보 포함 응답 여부")
    boolean isPrivacy;

    public QuestionResponseDto(String id, String type, String title, String description,
                               List<ObjectiveQuestionOption> options, boolean isRequired, boolean isPrivacy) {
        super(id, type, description);
        this.title = title;
        this.options = options;
        this.isRequired = isRequired;
        this.isPrivacy = isPrivacy;
    }

    public static QuestionResponseDto from(Question question) {
        if (question instanceof DescriptiveQuestion dq) {
            return fromDescriptiveQuestion(dq);
        } else if (question instanceof ObjectiveQuestion oq) {
            return fromObjectiveQuestion(oq);
        }

        throw new DtoConvertException(ErrorCode.SYSTEM_ERROR, "Question 엔티티를 DTO로 변환할 수 없습니다.: " + question.getClass());
    }

    private static QuestionResponseDto fromDescriptiveQuestion(DescriptiveQuestion question) {
        return new QuestionResponseDto(
                question.getUuid(),
                question.getQuestionType().displayName(),
                question.getTitle(),
                question.getDetail(),
                null,
                question.isRequired(),
                question.isPrivacy());
    }

    private static QuestionResponseDto fromObjectiveQuestion(ObjectiveQuestion question) {
        return new QuestionResponseDto(question.getUuid(),
                question.getQuestionType().displayName(),
                question.getTitle(),
                question.getDetail(),
                JsonConverter.toList(question.getQuestionOption(), ObjectiveQuestionOption.class),
                question.isRequired(),
                question.isPrivacy());
    }
}