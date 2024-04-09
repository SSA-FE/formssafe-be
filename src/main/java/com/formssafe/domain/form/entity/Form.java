package com.formssafe.domain.form.entity;

import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormRequest.FormUpdateDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.constants.FormConstants;
import com.formssafe.global.entity.BaseTimeEntity;
import com.formssafe.global.util.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Form extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "detail", columnDefinition = "text")
    private String detail;

    @Column(name = "image_url", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String imageUrl;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int expectTime;

    private boolean isEmailVisible;

    private LocalDateTime privacyDisposalDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FormStatus status;

    private int questionCnt;

    private int responseCnt;

    private boolean isTemp;

    private boolean isDeleted;

    @BatchSize(size = FormConstants.PAGE_SIZE)
    @OneToMany(mappedBy = "form")
    private List<FormTag> formTagList = new ArrayList<>();

    @OneToOne(mappedBy = "form", fetch = FetchType.LAZY)
    private Reward reward;

    @OneToMany(mappedBy = "form")
    private List<DescriptiveQuestion> descriptiveQuestionList = new ArrayList<>();

    @OneToMany(mappedBy = "form")
    private List<ObjectiveQuestion> objectiveQuestionList = new ArrayList<>();

    @OneToMany(mappedBy = "form")
    private List<Decoration> decorationList = new ArrayList<>();

    @OneToMany(mappedBy = "form")
    private List<RewardRecipient> rewardRecipientList = new ArrayList<>();

    @Builder
    private Form(Long id, User user, String title, String detail, List<String> imageUrl,
                 LocalDateTime startDate, LocalDateTime endDate, int expectTime, boolean isEmailVisible,
                 LocalDateTime privacyDisposalDate, FormStatus status, int questionCnt, int responseCnt, boolean isTemp,
                 boolean isDeleted) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.detail = detail;
        this.imageUrl = JsonConverter.toJson(imageUrl);
        this.startDate = startDate;
        this.endDate = endDate;
        this.expectTime = expectTime;
        this.isEmailVisible = isEmailVisible;
        this.privacyDisposalDate = privacyDisposalDate;
        this.status = status;
        this.questionCnt = questionCnt;
        this.responseCnt = responseCnt;
        this.isTemp = isTemp;
        this.isDeleted = isDeleted;
    }

    public void changeStatus(@NonNull FormStatus newStatus) {
        this.status = newStatus;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public static Form createTempForm(FormCreateDto formCreateDto, User user, LocalDateTime endDate, int questionCnt) {
        return Form.builder()
                .title(formCreateDto.title())
                .detail(formCreateDto.description())
                .imageUrl(formCreateDto.image())
                .user(user)
                .startDate(null)
                .endDate(endDate)
                .expectTime(formCreateDto.expectTime())
                .isEmailVisible(formCreateDto.emailVisibility())
                .privacyDisposalDate(formCreateDto.privacyDisposalDate())
                .questionCnt(questionCnt)
                .isTemp(true)
                .status(FormStatus.NOT_STARTED)
                .build();
    }

    public static Form createForm(FormCreateDto formCreateDto, User user, LocalDateTime startDate,
                                  LocalDateTime endDate, int questionCnt) {
        return Form.builder()
                .title(formCreateDto.title())
                .detail(formCreateDto.description())
                .imageUrl(formCreateDto.image())
                .user(user)
                .startDate(startDate)
                .endDate(endDate)
                .expectTime(formCreateDto.expectTime())
                .isEmailVisible(formCreateDto.emailVisibility())
                .privacyDisposalDate(formCreateDto.privacyDisposalDate())
                .questionCnt(questionCnt)
                .isTemp(false)
                .status(FormStatus.PROGRESS)
                .build();
    }

    public void updateToForm(FormUpdateDto request, LocalDateTime startDate, LocalDateTime endDate, int questionCnt) {
        this.title = request.title();
        this.detail = request.description();
        this.imageUrl = JsonConverter.toJson(request.image());
        this.startDate = startDate;
        this.endDate = endDate;
        this.expectTime = request.expectTime();
        this.isEmailVisible = request.emailVisibility();
        this.privacyDisposalDate = request.privacyDisposalDate();
        this.status = FormStatus.PROGRESS;
        this.questionCnt = questionCnt;
        this.isTemp = request.isTemp();
    }

    public void updateToTempForm(FormUpdateDto request, LocalDateTime endDate, int questionCnt) {
        this.title = request.title();
        this.detail = request.description();
        this.imageUrl = JsonConverter.toJson(request.image());
        this.startDate = null;
        this.endDate = endDate;
        this.expectTime = request.expectTime();
        this.isEmailVisible = request.emailVisibility();
        this.privacyDisposalDate = request.privacyDisposalDate();
        this.status = FormStatus.NOT_STARTED;
        this.questionCnt = questionCnt;
        this.isTemp = request.isTemp();
    }

    @Override
    public String toString() {
        return "Form{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", imageUrl=" + imageUrl +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", expectTime=" + expectTime +
                ", isVisibleEmail=" + isEmailVisible +
                ", privacyDisposalDate=" + privacyDisposalDate +
                ", status=" + status +
                ", isTemp=" + isTemp +
                ", isDeleted=" + isDeleted +
                ", tagList=" + formTagList +
                ", reward=" + reward +
                '}';
    }
}
