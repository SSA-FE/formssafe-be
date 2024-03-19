package com.formssafe.domain.question.entity;

import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DescriptiveQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DescriptiveQuestionType questionType;

    @Column(nullable = false)
    private String title;

    private String detail;

    private boolean isRequired;

    private boolean isPrivacy;

    @Builder
    private DescriptiveQuestion(Integer id, Form form, DescriptiveQuestionType questionType, String title,
                                String detail,
                                boolean isRequired, boolean isPrivacy) {
        this.id = id;
        this.form = form;
        this.questionType = questionType;
        this.title = title;
        this.detail = detail;
        this.isRequired = isRequired;
        this.isPrivacy = isPrivacy;
    }

    public Integer getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public DescriptiveQuestionType getQuestionType() {
        return questionType;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public boolean isPrivacy() {
        return isPrivacy;
    }
}
