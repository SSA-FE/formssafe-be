package com.formssafe.domain.submission.entity;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DescriptiveSubmission extends SubmissionResponse {
    @Column(nullable = false)
    private String content;

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
