package com.formssafe.domain.reward.dto;

import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public final class RewardResponse {

    public record RewardListDto(@Schema(description = "설문 경품 이름") String name,
                                @Schema(description = "설문 경품 카테고리") String category,
                                @Schema(description = "설문 경품 개수") int count) {

        public static RewardListDto from(Reward reward, RewardCategory rewardCategory) {
            return new RewardListDto(reward.getRewardName(),
                    rewardCategory.getRewardCategoryName(),
                    reward.getCount());
        }
    }
}
