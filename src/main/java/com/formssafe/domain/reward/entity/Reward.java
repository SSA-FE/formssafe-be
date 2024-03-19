package com.formssafe.domain.reward.entity;

import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Column(nullable = false, unique = true)
    private String rewardName;

    @Getter
    @ManyToOne
    @JoinColumn(name = "reward_category_id", nullable = false)
    private RewardCategory rewardCategory;

    @Getter
    @OneToOne
    private Form form;

    @Getter
    private int count;

    @Builder
    private Reward(Integer id, String rewardName, RewardCategory rewardCategory, Form form, int count) {
        this.id = id;
        this.rewardName = rewardName;
        this.rewardCategory = rewardCategory;
        this.form = form;
        this.count = count;
    }
}
