package com.formssafe.domain.question.entity;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.global.util.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjectiveQuestion extends Question {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObjectiveQuestionType questionType;

    @Column(name = "question_option", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String questionOption;

    @Builder
    private ObjectiveQuestion(Integer id,
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

    public ObjectiveQuestionType getQuestionType() {
        return questionType;
    }

    public String getQuestionOption() {
        return questionOption;
    }

    @Override
    public String toString() {
        return "ObjectiveQuestion{" + "id=" + id + ", form=" + form + ", questionType=" + questionType + ", title='"
                + title + '\'' + ", detail='" + detail + '\'' + ", questionOption='" + questionOption + '\''
                + ", isRequired=" + isRequired + ", isPrivacy=" + isPrivacy + '}';
    }
}
