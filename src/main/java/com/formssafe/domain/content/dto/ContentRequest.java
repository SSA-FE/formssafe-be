package com.formssafe.domain.content.dto;

import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionOption;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionType;
import com.formssafe.domain.form.entity.Form;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

public final class ContentRequest {

    @Schema(description = "설문 문항 생성 요청 DTO",
            requiredProperties = {"type", "title", "isRequired", "isPrivacy"})
    public record ContentCreateDto(@Schema(description = "설문 문항 타입") String type,
                                   @Schema(description = "설문 문항 질문") String title,
                                   @Schema(description = "설문 문항 설명") String description,
                                   @Schema(description = "객관식 문항일 시, 보기 목록") List<String> options,
                                   @Schema(description = "필수 응답 여부") boolean isRequired,
                                   @Schema(description = "개인 정보 포함 응답 여부") boolean isPrivacy) {

        public Decoration toDecoration(Form form, int position) {
            return Decoration.builder()
                    .form(form)
                    .type(DecorationType.from(type))
                    .detail(description)
                    .position(position)
                    .build();
        }

        public DescriptiveQuestion toDescriptiveQuestion(Form form, int position) {
            return DescriptiveQuestion.builder()
                    .form(form)
                    .questionType(DescriptiveQuestionType.from(type))
                    .title(title)
                    .detail(description)
                    .position(position)
                    .isRequired(isRequired)
                    .isPrivacy(isPrivacy)
                    .build();
        }

        public ObjectiveQuestion toObjectiveQuestion(Form form, int position) {
            List<ObjectiveQuestionOption> objectiveQuestionOptions = new ArrayList<>();
            for (int i = 0; i < options.size(); i++) {
                objectiveQuestionOptions.add(new ObjectiveQuestionOption(i + 1, options.get(i)));
            }

            return ObjectiveQuestion.builder()
                    .form(form)
                    .questionType(ObjectiveQuestionType.from(type))
                    .title(title)
                    .detail(description)
                    .position(position)
                    .questionOption(objectiveQuestionOptions)
                    .isRequired(isRequired)
                    .isPrivacy(isPrivacy)
                    .build();
        }

        @Override
        public String toString() {
            return "ContentCreateDto{" + "type='" + type + '\'' + ", title='" + title + '\'' + ", description='"
                    + description
                    + '\'' + ", options=" + options + ", isRequired=" + isRequired + ", isPrivacy="
                    + isPrivacy + '}';
        }
    }
}
