package com.formssafe.domain.reward.repository;

import com.formssafe.domain.reward.entity.RewardCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardCategoryRepository extends JpaRepository<RewardCategory, Long> {

    Optional<RewardCategory> findByRewardCategoryName(String categoryName);
}
