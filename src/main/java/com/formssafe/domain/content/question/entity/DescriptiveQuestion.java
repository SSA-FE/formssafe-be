package com.formssafe.domain.content.question.entity;

import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "descriptive_question", indexes = {
        @Index(name = "idx_uuid", columnList = "uuid")
})
public class DescriptiveQuestion extends Question {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DescriptiveQuestionType questionType;

    @Builder
    private DescriptiveQuestion(Long id,
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
}