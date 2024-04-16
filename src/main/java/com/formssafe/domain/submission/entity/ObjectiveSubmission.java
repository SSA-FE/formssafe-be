package com.formssafe.domain.submission.entity;

import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionOption;
import com.formssafe.global.util.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjectiveSubmission extends SubmissionResponse {
    @Column(name = "content", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String content;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private ObjectiveQuestion objectiveQuestion;

    @Builder
    private ObjectiveSubmission(Long id, Object content, Submission submission, ObjectiveQuestion objectiveQuestion,
                                int position) {
        this.id = id;
        this.content = JsonConverter.toJson(content);
        this.submission = submission;
        this.objectiveQuestion = objectiveQuestion;
        this.position = position;
    }

    public String getSelection() {
        List<ObjectiveQuestionOption> options = JsonConverter.toList(objectiveQuestion.getQuestionOption(),
                ObjectiveQuestionOption.class);
        List<Integer> responses = JsonConverter.toList(this.content, Integer.class);
        responses.sort(Integer::compareTo);

        return options.stream()
                .filter(option -> responses.contains(option.getId()))
                .map(ObjectiveQuestionOption::getDetail)
                .collect(Collectors.joining(","));
    }
}
