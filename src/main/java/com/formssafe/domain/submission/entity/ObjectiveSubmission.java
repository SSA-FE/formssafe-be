package com.formssafe.domain.submission.entity;

import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.global.util.JsonConverter;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name ="response_id", nullable = false)
    private Submission submission;

    @ManyToOne
    @JoinColumn(name="question_id", nullable = false)
    private ObjectiveQuestion objectiveQuestion;

    @Builder
    private ObjectiveSubmission(Long id, Object content, Submission submission, ObjectiveQuestion objectiveQuestion){
        this.id = id;
        this.content = JsonConverter.toJson(content);
        this.submission = submission;
        this.objectiveQuestion = objectiveQuestion;
    }
}
