package com.formssafe.domain.oauth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "설문 등록 요청 DTO",
        requiredProperties ={"code"})
public record AuthCode(
        @Schema(description = "구글 oauth에서 발급 받은 code")
        String code
)
{ }
