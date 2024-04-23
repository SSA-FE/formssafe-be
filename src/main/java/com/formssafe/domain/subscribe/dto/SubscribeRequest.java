package com.formssafe.domain.subscribe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public final class SubscribeRequest {
    public record RewardListDto(
            @Schema(description = "사용자가 선택한 경품 카테고리 id") List<Long> reward
    ) {
        @Override
        public String toString() {
            return "RewardDto{reward =" + reward + "}";
        }
    }
}
