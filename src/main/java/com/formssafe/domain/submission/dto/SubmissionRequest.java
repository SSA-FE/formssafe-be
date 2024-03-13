package com.formssafe.domain.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "설문 응답 저장", requiredProperties = {"formId", "submissions", "isTemp"})
public record SubmissionRequest(
    @Schema(description = "설문 폼 id") int formId,
    @Schema(description = "응답 리스트") List<Submission> submissions,
    @Schema(description = "임시 저장 여부") boolean isTemp
){}