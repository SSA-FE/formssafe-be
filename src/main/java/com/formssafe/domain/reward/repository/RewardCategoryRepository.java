package com.formssafe.domain.reward.repository;

import com.formssafe.domain.reward.entity.RewardCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RewardCategoryRepository extends JpaRepository<RewardCategory, Long> {

    Optional<RewardCategory> findByRewardCategoryName(String categoryName);

    @Query("""
            SELECT rc.id, rc.rewardCategoryName, CASE WHEN s.id IS NOT NULL THEN true ELSE false END
            FROM RewardCategory rc
            LEFT JOIN Subscribe s ON rc.id = s.rewardCategory.id AND s.user.id =:userId
            """)
    List<Object[]> getRewardCategoryWithSubscribe(@Param("userId") Long userId);
}
