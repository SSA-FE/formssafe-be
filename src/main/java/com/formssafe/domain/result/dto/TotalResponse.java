package com.formssafe.domain.result.dto;

import com.formssafe.domain.submission.dto.Response;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record TotalResponse (
    @Schema(description = "질문 ID") int responseId,
//  @Schema(description = "사용자 id 및 nickname")  private UserDto user,
    @Schema(description = "설문 응답 리스트") List<Response> responses,
    @Schema(description = "응답 시간") LocalDateTime responsedAt
){}
