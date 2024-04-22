package com.formssafe.domain.content.dto;

import com.formssafe.domain.content.decoration.dto.DecorationResponseDto;
import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.content.question.dto.QuestionResponseDto;
import com.formssafe.domain.content.question.entity.Question;
import com.formssafe.global.error.type.DtoConvertException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public abstract class ContentResponseDto {
    @Schema(description = "설문 문항 id") String id;
    @Schema(description = "설문 문항 타입") String type;
    @Schema(description = "설문 문항 설명") String description;

    protected ContentResponseDto(String id, String type, String description){
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public static ContentResponseDto from(Content content){
        if (content instanceof Question question){
            return QuestionResponseDto.from(question);
        }
        else if (content instanceof Decoration decoration){
            return DecorationResponseDto.from(decoration);
        }
        throw new DtoConvertException("Question 엔티티를 DTO로 변환할 수 없습니다.: " + content.getClass());
    }
}
