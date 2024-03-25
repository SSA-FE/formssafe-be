package com.formssafe.domain.reward.repository;

import com.formssafe.domain.reward.entity.RewardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardCategoryRepository extends JpaRepository<RewardCategory, Long> {
}
