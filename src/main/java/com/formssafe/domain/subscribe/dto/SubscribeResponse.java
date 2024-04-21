package com.formssafe.domain.subscribe.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public final class SubscribeResponse {
    public record CategoryListDto(
            @Schema(description = "경품 ID") Long id,
            @Schema(description = "경품 명") String name,
            @Schema(description = "경품 구독 여부") Boolean isSelected
    ) {
        public static CategoryListDto fromObject(Object[] object) {
            return new CategoryListDto((Long) object[0], (String) object[1], (Boolean) object[2]);
        }

        @Override
        public String toString() {
            return "CategoryListDto{id = " + id + ", name = " + name + ", isSelected = " + isSelected + "}";
        }
    }
}
