package com.formssafe.domain.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record Submission(
        @Schema(description = "질문 ID") int questionId,
        @Schema(description = "응답, string, int, int[]가능") Object content
){}
