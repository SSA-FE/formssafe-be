package com.formssafe.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserDto(
        @Schema(description = "사용자 ID") int userId,
        @Schema(description = "사용자 별명") String nickname,
        @Schema(description = "이미지 URL") String imageUrl,
        @Schema(description = "이메일 주소") String email) {
}
