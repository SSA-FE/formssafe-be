package com.formssafe.domain.submission.entity;

import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.global.util.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjectiveSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String content;

    @Schema(description = "제출한 설문의 position")
    private int position;

    @ManyToOne
    @JoinColumn(name = "response_id", nullable = false)
    private Submission submission;

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
}
