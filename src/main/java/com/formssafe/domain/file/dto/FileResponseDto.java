package com.formssafe.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FileResponseDto (
        @Schema(description = "제공되는 presigned-url") String path){
    public static FileResponseDto convertStringToDto(String fileName){
        return new FileResponseDto(fileName);
    }
}
