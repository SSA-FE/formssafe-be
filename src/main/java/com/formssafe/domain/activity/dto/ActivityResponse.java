package com.formssafe.domain.activity.dto;

import com.formssafe.domain.reward.dto.RewardResponse;
import com.formssafe.domain.tag.dto.TagResponse;
import com.formssafe.domain.user.dto.UserResponse;
import java.time.LocalDateTime;

public final class ActivityResponse {

    private ActivityResponse() {
    }

    public record FormListDto(Long id,
                              String title,
                              String thumbnail,
                              UserResponse.List author,
                              int expectTime,
                              int questionCnt,
                              int responseCnt,
                              LocalDateTime startDate,
                              LocalDateTime endDate,
                              RewardResponse.ListDto reward,
                              TagResponse.CountDto[] tags,
                              String status) {
    }
}
