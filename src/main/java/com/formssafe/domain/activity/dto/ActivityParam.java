package com.formssafe.domain.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

public final class ActivityParam {

    @Schema(description = "내 활동 조회 시 검색 파라미터")
    @Slf4j
    public record SearchDto(@Schema(description = "검색어")
                            String keyword,
                            @Schema(description = "정렬 기준", defaultValue = "create date", allowableValues = {
                                    "createDate",
                                    "endDate"})
                            String sort,
                            @Schema(description = "카테고리")
                            List<String> category,
                            @Schema(description = "설문 상태", allowableValues = {"not_started", "progress", "done",
                                    "rewarded"})
                            String status,
                            @Schema(description = "태그")
                            List<String> tag,
                            @Schema(description = "마지막 formId")
                            Long top,
                            @Schema(description = "SortType이 startDate, 마지막 startDate")
                            LocalDateTime startDate,
                            @Schema(description = "SortType이 EndDate일때, 마지막 endDate")
                            LocalDateTime endDate,
                            @Schema(description = "SortType이 responseCnt일 때, 마지막 responseCnt")
                            Integer responseCnt,
                            @Schema(description = "임시 여부")
                            Boolean temp
    ) {

        public static SearchDto createNull() {
            return new SearchDto(null, null, null, null, null, null, null, null, null, null);
        }
    }
}
