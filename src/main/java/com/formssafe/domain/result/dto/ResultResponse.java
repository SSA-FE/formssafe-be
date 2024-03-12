package com.formssafe.domain.result.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ResultResponse (
        @Schema(description = "설문 ID") int formId,
        @Schema(description = "응답 갯수 ID") int responseCnt,
        @Schema(description = "전체 응답 결과 ID") List<TotalResponse> totalResponses
) {}
