package com.formssafe.domain.tag.dto;

import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public final class TagResponse {

    public record TagCountDto(@Schema(description = "설문 태그 id") Long id,
                              @Schema(description = "설문 태그 이름") String name,
                              @Schema(description = "설문 태그가 사용된 개수") int count) {
        public static List<TagCountDto> from(List<FormTag> formTag){
            List<TagCountDto> list = new ArrayList<>();
            for(int t=0; t<formTag.size(); t++){
                list.add(new TagCountDto(formTag.get(t).getTag().getId(), formTag.get(t).getTag().getTagName(), formTag.get(t).getTag().getCount()));
            }
            return list;
        }
    }

    public record TagListDto(@Schema(description = "설문 태그 id") Long id,
                             @Schema(description = "설문 태그 이름") String name) {

        public static TagListDto from(Tag tag) {
            return new TagListDto(tag.getId(), tag.getTagName());
        }
    }
}


