package com.formssafe.domain.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formssafe.domain.content.decoration.dto.DecorationResponse;
import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.content.question.dto.QuestionResponse;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionOption;
import com.formssafe.domain.content.question.entity.Question;
import com.formssafe.global.exception.type.DtoConvertException;
import com.formssafe.global.util.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public final class ContentResponse {
    public record ContentDetailDto(@Schema(description = "설문 항목 id")
                                   String id,
                                   @Schema(description = "설문 항목 타입")
                                   String type,
                                   @Schema(description = "설문 항목 제목")
                                   @JsonInclude(JsonInclude.Include.NON_NULL)
                                   String title,
                                   @Schema(description = "설문 문항 설명")
                                   String description,
                                   @Schema(description = "객관식 문항일 시, 보기 목록")
                                   @JsonInclude(JsonInclude.Include.NON_NULL)
                                   List<ObjectiveQuestionOption> options,
                                   @Schema(description = "필수 응답 여부")
                                   @JsonInclude(JsonInclude.Include.NON_NULL)
                                   boolean isRequired,
                                   @Schema(description = "개인 정보 포함 응답 여부")
                                   @JsonInclude(JsonInclude.Include.NON_NULL)
                                   boolean isPrivacy) {

        public static ContentDetailDto from(Content content){
            if (content instanceof Question question){
                if (question instanceof DescriptiveQuestion dq) {
                    return fromDescriptiveQuestion(dq);
                } else if (question instanceof ObjectiveQuestion oq) {
                    return fromObjectiveQuestion(oq);
                }
            }
            else if (content instanceof Decoration decoration){
                return fromDecoration(decoration);
            }
            throw new DtoConvertException("Question 엔티티를 DTO로 변환할 수 없습니다.: " + content.getClass());
        }

        private static ContentDetailDto fromDescriptiveQuestion(DescriptiveQuestion question) {
            return new ContentDetailDto(question.getUuid(),
                    question.getQuestionType().displayName(),
                    question.getTitle(),
                    question.getDetail(),
                    null,
                    question.isRequired(),
                    question.isPrivacy());
        }

        private static ContentDetailDto fromObjectiveQuestion(ObjectiveQuestion question) {
            return new ContentDetailDto(question.getUuid(),
                    question.getQuestionType().displayName(),
                    question.getTitle(),
                    question.getDetail(),
                    JsonConverter.toList(question.getQuestionOption(), ObjectiveQuestionOption.class),
                    question.isRequired(),
                    question.isPrivacy());
        }

        private static ContentDetailDto fromDecoration(Decoration decoration){
            return new ContentDetailDto(decoration.getUuid(), decoration.getType(),null, decoration.getDetail(), null, false, false);
        }
    }

}
