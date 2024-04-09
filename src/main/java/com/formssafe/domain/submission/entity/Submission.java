package com.formssafe.domain.submission.entity;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Schema(description = "설문 임시 저장 여부")
    boolean isTemp;

    @Schema(description = "최종 제출 시간")
    LocalDateTime submitTime;

    @OneToMany(mappedBy = "submission")
    List<DescriptiveSubmission> descriptiveSubmissionList = new ArrayList<>();

    @OneToMany(mappedBy = "submission")
    List<ObjectiveSubmission> objectiveSubmissionList = new ArrayList<>();

    @Builder
    private Submission(Long id, User user, Form form, boolean isTemp, LocalDateTime submitTime) {
        this.id = id;
        this.user = user;
        this.form = form;
        this.isTemp = isTemp;
        this.submitTime = submitTime;
    }

    public void update(boolean isTemp, LocalDateTime submitTime) {
        this.isTemp = isTemp;
        this.submitTime = submitTime;
    }
}
