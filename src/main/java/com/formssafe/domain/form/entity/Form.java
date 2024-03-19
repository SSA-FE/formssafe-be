package com.formssafe.domain.form.entity;

import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.entity.BaseTimeEntity;
import com.formssafe.global.util.JsonListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Form extends BaseTimeEntity {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Column(name = "title", columnDefinition = "text", nullable = false)
    private String title;

    @Getter
    @Column(name = "detail", columnDefinition = "text")
    private String detail;

    @Getter
    @Column(name = "image_url", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String imageUrl;

    @Getter
    @Column(nullable = false)
    private LocalDateTime startDate;

    @Getter
    @Column(nullable = false)
    private LocalDateTime endDate;

    @Getter
    private int expectTime;

    private boolean isEmailVisible;

    @Getter
    private LocalDateTime privacyDisposalDate;

    @Getter
    @Column(nullable = false)
    private FormStatus status;

    private boolean isTemp;

    private boolean isDeleted;

    @Getter
    @OneToMany(mappedBy = "form")
    private List<FormTag> tagList = new ArrayList<>();

    @Getter
    @OneToOne(mappedBy = "form")
    private Reward reward;

    @Builder
    private Form(Integer id, User user, String title, String detail, List<String> imageUrl, LocalDateTime startDate,
                 LocalDateTime endDate, int expectTime, boolean isEmailVisible, LocalDateTime privacyDisposalDate,
                 FormStatus status, boolean isTemp, boolean isDeleted) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.detail = detail;
        this.imageUrl = JsonListConverter.convertToDatabaseColumn(imageUrl);
        this.startDate = startDate;
        this.endDate = endDate;
        this.expectTime = expectTime;
        this.isEmailVisible = isEmailVisible;
        this.privacyDisposalDate = privacyDisposalDate;
        this.status = status;
        this.isTemp = isTemp;
        this.isDeleted = isDeleted;
    }

    public boolean isEmailVisible() {
        return isEmailVisible;
    }

    public boolean isTemp() {
        return isTemp;
    }

    public boolean isDeleted() {
        return isDeleted;
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
                ", tagList=" + tagList +
                '}';
    }
}
