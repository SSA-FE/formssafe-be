package com.formssafe.domain.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public final class TagResponse {

    public record TagCountDto(@Schema(description = "설문 태그 id") Long id,
                              @Schema(description = "설문 태그 이름") String name,
                              @Schema(description = "설문 태그가 사용된 개수") int count) {
    }

    public record TagListDto(@Schema(description = "설문 태그 id") Long id,
                             @Schema(description = "설문 태그 이름") String name) {
    }
}


