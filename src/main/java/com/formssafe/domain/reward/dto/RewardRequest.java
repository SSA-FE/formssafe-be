package com.formssafe.domain.reward.dto;

public final class RewardRequest {

    public record CreateDto(String name,
                            String category,
                            int count) {
    }
}
