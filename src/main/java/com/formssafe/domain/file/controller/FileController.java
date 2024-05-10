package com.formssafe.domain.file.controller;

import com.formssafe.domain.file.dto.FileResponseDto;
import com.formssafe.domain.file.service.FileService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.global.error.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "files", description = "s3 presigned-url 받아오기")
@RestController
@RequestMapping("/v1/files")
@Slf4j
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @Operation(summary = "이미지 업로드를 위한 presigned-url 발급")
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/upload/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    FileResponseDto createPresignedUrl(@PathVariable String fileName, @AuthenticationPrincipal LoginUserDto loginUser) {
        return fileService.createPresignedUrl("image", fileName);
    }
}
