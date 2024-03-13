package com.formssafe.domain.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public final class QuestionResponse {

    public record QuestionDetailDto(@Schema(description = "설문 문항 id") Long id,
                                    @Schema(description = "설문 문항 타입") String type,
                                    @Schema(description = "설문 문항 질문") String title,
                                    @Schema(description = "설문 문항 설명") String description,
                                    @Schema(description = "객관식 문항일 시, 보기 목록") String[] options,
                                    @Schema(description = "필수 응답 여부") boolean isRequired,
                                    @Schema(description = "개인 정보 포함 응답 여부") boolean isPrivacy) {
    }
}

