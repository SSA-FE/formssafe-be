package com.formssafe.domain.submission.entity;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name ="response_id", nullable = false)
    private Submission submission;

    @ManyToOne
    @JoinColumn(name="question_id", nullable = false)
    private DescriptiveQuestion descriptiveQuestion;

    @Builder
    private DescriptiveSubmission(Long id, String content, Submission submission, DescriptiveQuestion descriptiveQuestion){
        this.id = id;
        this.content = content;
        this.submission = submission;
        this.descriptiveQuestion = descriptiveQuestion;
    }
}
