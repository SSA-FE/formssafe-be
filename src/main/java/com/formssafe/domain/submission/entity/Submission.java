package com.formssafe.domain.submission.entity;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Submission extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="form_id", nullable = false)
    private Form form;

    @Schema(description = "설문 임시 저장 여부")
    boolean isTemp;

    @OneToMany(mappedBy = "submission")
    List<DescriptiveSubmission> descriptiveSubmissionList = new ArrayList<>();

    @OneToMany(mappedBy = "submission")
    List<ObjectiveSubmission> objectiveSubmissionList = new ArrayList<>();

    @Builder
    private Submission(Long id, User user, Form form, boolean isTemp){
        this.id = id;
        this.user = user;
        this.form = form;
        this.isTemp = isTemp;
    }
}