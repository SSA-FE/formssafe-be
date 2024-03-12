package com.formssafe.domain.reward.dto;

public final class RewardResponse {

    public record ListDto(String name,
                          String category,
                          int count) {
    }
}
