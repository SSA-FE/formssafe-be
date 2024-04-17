package com.formssafe.domain.submission.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class SubmissionResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Schema(description = "제출한 설문의 position")
    protected int position;

    @ManyToOne
    @JoinColumn(name = "response_id", nullable = false)
    protected Submission submission;

    protected SubmissionResponse(Long id, int position, Submission submission) {
        this.id = id;
        this.position = position;
        this.submission = submission;
    }
}
