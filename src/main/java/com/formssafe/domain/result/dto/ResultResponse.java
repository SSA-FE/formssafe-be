package com.formssafe.domain.result.dto;

import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionDetailResponseDto;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public final class ResultResponse {
    private ResultResponse() {
    }

    public record ResultResponseDto(
            @Schema(description = "설문 ID") Long formId,
            @Schema(description = "응답 갯수 ID") int responseCnt,
            @Schema(description = "전체 응답 결과 list") List<TotalResponse> totalResponses) {
        public static ResultResponseDto from(Long formId, int responseCnt, List<TotalResponse> totalResponses) {
            return new ResultResponseDto(formId, responseCnt, totalResponses);
        }
    }

    public record TotalResponse(
            @Schema(description = "질문 ID") Long responseId,
            @Schema(description = "사용자 id 및 nickname") UserListDto user,
            @Schema(description = "설문 응답 리스트") List<SubmissionDetailResponseDto> responses,
            @Schema(description = "응답 시간") LocalDateTime responsedAt
    ) {
        public static TotalResponse from(Long responseId, UserListDto user, List<SubmissionDetailResponseDto> responses,
                                         LocalDateTime responsedAt) {
            return new TotalResponse(responseId, user, responses, responsedAt);
        }
    }
}
