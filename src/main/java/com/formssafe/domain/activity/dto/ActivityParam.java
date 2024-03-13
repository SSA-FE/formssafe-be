package com.formssafe.domain.activity.dto;

public final class ActivityParam {

    public record SearchDto(String keyword,
                            String sort,
                            String[] category,
                            String status,
                            String[] tag,
                            Long pageNum) {
    }
}
