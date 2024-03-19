package com.formssafe.domain.reward.repository;

import com.formssafe.domain.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, Integer> {
}
