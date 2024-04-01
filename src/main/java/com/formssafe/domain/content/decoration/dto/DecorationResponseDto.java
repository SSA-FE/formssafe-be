package com.formssafe.domain.content.decoration.dto;

import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.dto.ContentResponseDto;
import lombok.Getter;

@Getter
public class DecorationResponseDto extends ContentResponseDto {
    public DecorationResponseDto(String id, DecorationType type, String description){
        super(id, type.displayName(), description);
    }

    public static DecorationResponseDto from(Decoration decoration){
        return new DecorationResponseDto(decoration.getUuid(), decoration.getType(), decoration.getDetail());
    }
}
