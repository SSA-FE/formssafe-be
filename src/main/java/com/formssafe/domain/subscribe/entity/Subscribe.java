package com.formssafe.domain.subscribe.entity;

import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reward_category_id", nullable = false)
    private RewardCategory rewardCategory;

    @Builder
    private Subscribe(Long id, User user, RewardCategory rewardCategory) {
        this.id = id;
        this.user = user;
        this.rewardCategory = rewardCategory;
    }
}
