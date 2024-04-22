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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "descriptive_question", indexes = {
        @Index(name = "idx_uuid", columnList = "uuid")
})
@EqualsAndHashCode(callSuper = true)
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

    private DescriptiveQuestion(String uuid,
                                DescriptiveQuestionType questionType,
                                String title,
                                String detail,
                                int position,
                                boolean isRequired,
                                boolean isPrivacy) {
        super(uuid, title, detail, position, isRequired, isPrivacy);
        this.questionType = questionType;
    }

    public static DescriptiveQuestion of(String uuid, DescriptiveQuestionType questionType, String title,
                                         String detail, boolean isPrivacy, boolean isRequired, int position) {
        return new DescriptiveQuestion(uuid, questionType, title, detail, position, isRequired, isPrivacy);
    }

    @Override
    public String toString() {
        return "DescriptiveQuestion{" +
                "questionType=" + questionType +
                ", title='" + title + '\'' +
                ", isRequired=" + isRequired +
                ", isPrivacy=" + isPrivacy +
                ", id=" + id +
                ", uuid='" + uuid + '\'' +
                ", form=" + form +
                ", detail='" + detail + '\'' +
                ", position=" + position +
                '}';
    }
}
