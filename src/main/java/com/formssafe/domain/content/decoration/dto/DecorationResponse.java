package com.formssafe.domain.content.decoration.dto;

import com.formssafe.domain.content.decoration.entity.Decoration;
import io.swagger.v3.oas.annotations.media.Schema;

public final class DecorationResponse {
    public record DecorationDetailDto(@Schema(description = "텍스트 블록 id")
                                      String id,
                                      @Schema(description = "블록 type, text로 고정")
                                      String type,
                                      @Schema(description = "텍스트 블록 설명")
                                      String description){
        public static DecorationDetailDto from(Decoration decoration){
            return new DecorationDetailDto(decoration.getUuid(), decoration.getType(), decoration.getDetail());
        }
    }
}
