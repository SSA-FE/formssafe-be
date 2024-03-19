package com.formssafe.domain.reward.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String rewardCategoryName;

    @Builder
    private RewardCategory(Integer id, String rewardCategoryName) {
        this.id = id;
        this.rewardCategoryName = rewardCategoryName;
    }

    public Integer getId() {
        return id;
    }

    public String getRewardCategoryName() {
        return rewardCategoryName;
    }
}
