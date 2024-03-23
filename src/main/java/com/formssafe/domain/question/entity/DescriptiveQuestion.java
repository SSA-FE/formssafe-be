package com.formssafe.domain.question.entity;

import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DescriptiveQuestion extends Question {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DescriptiveQuestionType questionType;

    @Builder
    private DescriptiveQuestion(Integer id,
                                Form form,
                                DescriptiveQuestionType questionType,
                                String title,
                                String detail,
                                int position,
                                boolean isRequired,
                                boolean isPrivacy) {
        super(id, form, title, detail, position, isRequired, isPrivacy);
        this.questionType = questionType;
    }

    public DescriptiveQuestionType getQuestionType() {
        return questionType;
    }
}
