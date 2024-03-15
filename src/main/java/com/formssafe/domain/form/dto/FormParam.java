package com.formssafe.domain.form.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public final class FormParam {

    @Schema(description = "설문 목록 조회 시 검색 파라미터")
    public record SearchDto(@Schema(description = "검색어")
                            String keyword,
                            @Schema(description = "정렬 기준", defaultValue = "create date", allowableValues = {
                                    "create date",
                                    "end date", "submissions"})
                            String sort,
                            @Schema(description = "카테고리")
                            String[] category,
                            @Schema(description = "설문 상태", allowableValues = {"not started", "progress", "done",
                                    "rewarded"})
                            String status,
                            @Schema(description = "태그")
                            String[] tag,
                            @Schema(description = "페이지 번호")
                            Long pageNum) {
    }
}
