package com.formssafe.domain.form.dto;

import com.formssafe.domain.question.dto.QuestionResponse;
import com.formssafe.domain.reward.dto.RewardResponse;
import com.formssafe.domain.tag.dto.TagResponse;
import com.formssafe.domain.user.dto.UserResponse;
import java.time.LocalDateTime;

public final class FormResponse {

    private FormResponse() {
    }

    public record DetailDto(Long id,
                            String title,
                            String description,
                            String[] image,
                            UserResponse.List author,
                            LocalDateTime startDate,
                            LocalDateTime endDate,
                            int expectTime,
                            boolean emailVisibility,
                            LocalDateTime privacyDisposalDate,
                            QuestionResponse.DetailDto[] questions,
                            RewardResponse.ListDto reward,
                            TagResponse.ListDto[] tags,
                            String status,
                            int responseCnt,
                            UserResponse.List[] recipients) {
    }

    public record ListDto(Long id,
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

