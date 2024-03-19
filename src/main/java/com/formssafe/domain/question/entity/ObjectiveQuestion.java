package com.formssafe.domain.question.entity;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.global.util.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjectiveQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObjectiveQuestionType questionType;

    @Column(nullable = false)
    private String title;

    private String detail;

    @Column(name = "question_option", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String questionOption;

    private boolean isRequired;

    private boolean isPrivacy;

    @Builder
    private ObjectiveQuestion(Integer id, Form form, ObjectiveQuestionType questionType, String title, String detail,
                              List<ObjectiveQuestionOption> questionOption, boolean isRequired, boolean isPrivacy) {
        this.id = id;
        this.form = form;
        this.questionType = questionType;
        this.title = title;
        this.detail = detail;
        this.questionOption = JsonConverter.toJson(questionOption);
        this.isRequired = isRequired;
        this.isPrivacy = isPrivacy;
    }

    public Integer getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public ObjectiveQuestionType getQuestionType() {
        return questionType;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getQuestionOption() {
        return questionOption;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public boolean isPrivacy() {
        return isPrivacy;
    }

    @Override
    public String toString() {
        return "ObjectiveQuestion{" +
                "id=" + id +
                ", form=" + form +
                ", questionType=" + questionType +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", questionOption='" + questionOption + '\'' +
                ", isRequired=" + isRequired +
                ", isPrivacy=" + isPrivacy +
                '}';
    }
}
