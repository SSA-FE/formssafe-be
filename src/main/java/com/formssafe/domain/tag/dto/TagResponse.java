package com.formssafe.domain.tag.dto;

import com.formssafe.domain.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

public final class TagResponse {

    public record TagCountDto(@Schema(description = "설문 태그 id") Integer id,
                              @Schema(description = "설문 태그 이름") String name,
                              @Schema(description = "설문 태그가 사용된 개수") int count) {
    }

    public record TagListDto(@Schema(description = "설문 태그 id") Integer id,
                             @Schema(description = "설문 태그 이름") String name) {

        public static TagListDto from(Tag tag) {
            return new TagListDto(tag.getId(), tag.getTagName());
        }
    }
}


