package com.formssafe.domain.activity.dto;

import com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record SelfSubmissionResponse (
        @Schema(description = "설문 폼 id") int formId,
        @Schema(description = "응답 리스트") List<SubmissionDetailDto> responses,
        @Schema(description = "임시 저장 여부") boolean isTemp
){}