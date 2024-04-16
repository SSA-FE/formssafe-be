package com.formssafe.domain.submission.entity;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DescriptiveSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Schema(description = "제출한 설문의 position")
    private int position;

    @ManyToOne
    @JoinColumn(name = "response_id", nullable = false)
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private DescriptiveQuestion descriptiveQuestion;

    @Builder
    private DescriptiveSubmission(Long id, String content, Submission submission,
                                  DescriptiveQuestion descriptiveQuestion, int position) {
        this.id = id;
        this.content = content;
        this.submission = submission;
        this.descriptiveQuestion = descriptiveQuestion;
        this.position = position;
    }
}
