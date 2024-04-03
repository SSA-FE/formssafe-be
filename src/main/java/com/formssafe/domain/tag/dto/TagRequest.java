package com.formssafe.domain.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public final class TagRequest {

    public record TagUpdateDto(@Schema(description = "설문 태그 이름") String name) {
    }
}
