package com.formssafe.domain.question.entity;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.global.util.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "objective_question", indexes = {
        @Index(name = "idx_uuid", columnList = "uuid")
})
public class ObjectiveQuestion extends Question {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObjectiveQuestionType questionType;

    @Column(name = "question_option", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String questionOption;

    @Builder
    private ObjectiveQuestion(Long id,
                              Form form,
                              ObjectiveQuestionType questionType,
                              String title,
                              String detail,
                              int position,
                              List<ObjectiveQuestionOption> questionOption,
                              boolean isRequired,
                              boolean isPrivacy) {
        super(id, form, title, detail, position, isRequired, isPrivacy);
        this.questionType = questionType;
        this.questionOption = JsonConverter.toJson(questionOption);
    }

    @Override
    public String toString() {
        return "ObjectiveQuestion{" + "id=" + id + ", form=" + form + ", questionType=" + questionType + ", title='"
                + title + '\'' + ", detail='" + detail + '\'' + ", questionOption='" + questionOption + '\''
                + ", isRequired=" + isRequired + ", isPrivacy=" + isPrivacy + '}';
    }
}