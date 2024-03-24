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
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rewardName;

    @ManyToOne
    @JoinColumn(name = "reward_category_id", nullable = false)
    private RewardCategory rewardCategory;

    @OneToOne
    private Form form;

    private int count;

    @Builder
    private Reward(Long id, String rewardName, RewardCategory rewardCategory, Form form, int count) {
        this.id = id;
        this.rewardName = rewardName;
        this.rewardCategory = rewardCategory;
        this.form = form;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public String getRewardName() {
        return rewardName;
    }

    public RewardCategory getRewardCategory() {
        return rewardCategory;
    }

    public Form getForm() {
        return form;
    }

    public int getCount() {
        return count;
    }
}
