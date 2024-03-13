package com.formssafe.domain.reward.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public final class RewardRequest {

    @Schema(description = "설문 경품 생성 요청 DTO",
            requiredProperties = {"name", "category", "count"})
    public record RewardCreateDto(@Schema(description = "설문 경품 이름") String name,
                                  @Schema(description = "설문 경품 카테고리") String category,
                                  @Schema(description = "설문 경품 개수") int count) {
    }
}
