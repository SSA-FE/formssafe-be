package com.formssafe.domain.form.dto;

import com.formssafe.domain.question.dto.QuestionRequest;
import com.formssafe.domain.reward.dto.RewardRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

public final class FormRequest {

    private FormRequest() {
    }

    public record CreateDto(String title,
                            String description,
                            String[] image,
                            LocalDateTime startDate,
                            LocalDateTime endDate,
                            int expectTime,
                            boolean emailVisibility,
                            LocalDateTime privacyDisposalDate,
                            QuestionRequest.CreateDto[] questions,
                            String[] tags,
                            RewardRequest.CreateDto reward,
                            boolean isTemp) {

        @Override
        public String toString() {
            return "CreateDto{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", image=" + Arrays.toString(image) +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", expectTime=" + expectTime +
                    ", emailVisibility=" + emailVisibility +
                    ", privacyDisposalDate=" + privacyDisposalDate +
                    ", questions=" + Arrays.toString(questions) +
                    ", tags=" + Arrays.toString(tags) +
                    ", reward=" + reward +
                    ", isTemp=" + isTemp +
                    '}';
        }
    }
}