package com.formssafe.domain.activity.dto;

import com.formssafe.domain.reward.dto.RewardResponse.RewardListDto;
import com.formssafe.domain.tag.dto.TagResponse.TagCountDto;
import com.formssafe.domain.user.dto.UserResponse.UserAuthorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public final class ActivityResponse {

    private ActivityResponse() {
    }

    @Schema(description = "설문 목록 조회 응답")
    public record FormListDto(@Schema(description = "설문 id")
                              Long id,
                              @Schema(description = "설문 제목")
                              String title,
                              @Schema(description = "설문 썸네일")
                              String thumbnail,
                              @Schema(description = "설문 등록자")
                              UserAuthorDto author,
                              @Schema(description = "설문 참여 예상 시간")
                              int expectTime,
                              @Schema(description = "설문 문항 개수")
                              int questionCnt,
                              @Schema(description = "설문 응답 개수")
                              int responseCnt,
                              @Schema(description = "설문 시작 시각")
                              LocalDateTime startDate,
                              @Schema(description = "설문 마감 시각")
                              LocalDateTime endDate,
                              @Schema(description = "설문 참여 시 받을 수 있는 경품")
                              RewardListDto reward,
                              @Schema(description = "설문 태그 목록")
                              TagCountDto[] tags,
                              @Schema(description = "설문 상태")
                              String status) {
    }
}
