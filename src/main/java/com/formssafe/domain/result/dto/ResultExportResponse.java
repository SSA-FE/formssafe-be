package com.formssafe.domain.result.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public final class ResultExportResponse {

    public record ResultExportRow(@Schema(description = "응답 시각") LocalDateTime responseAt,
                                  @Schema(description = "응답자") String nickname,
                                  @Schema(description = "응답 리스트") List<String> responses) {
    }
}


