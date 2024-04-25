package com.formssafe.domain.activity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formssafe.domain.form.dto.FormResponse.FormCursorDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.reward.dto.RewardResponse.RewardDto;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionDetailResponseDto;
import com.formssafe.domain.tag.dto.TagResponse.TagCountDto;
import com.formssafe.domain.user.dto.UserResponse.UserAuthorDto;
import com.formssafe.global.util.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public final class ActivityResponse {

    private ActivityResponse() {
    }

    @Schema(description = "설문 목록 조회 응답")
    public record FormListDto(@Schema(description = "설문 id")
                              Long id,
                              @Schema(description = "설문 제목")
                              String title,
                              @Schema(description = "설문 설명")
                              String description,
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
                              RewardDto reward,
                              @Schema(description = "설문 태그 목록")
                              List<TagCountDto> tags,
                              @Schema(description = "설문 상태")
                              String status,
                              @Schema(description = "설문 임시 등록 여부")
                              boolean isTemp) {

        public static FormListDto from(Form form) {
            String imageUrl = null;
            if (!form.getImageUrl().equals("null")) {
                imageUrl = JsonConverter.toList(form.getImageUrl(), String.class).get(0);
            }

            RewardDto rewardDto = null;
            if (form.getReward() != null) {
                rewardDto = RewardDto.from(form.getReward(), form.getReward().getRewardCategory());
            }

            List<TagCountDto> tagCountDtos = null;
            if (form.getFormTagList() != null) {
                tagCountDtos = TagCountDto.from(form.getFormTagList());
            }

            return new FormListDto(form.getId(),
                    form.getTitle(),
                    form.getDetail(),
                    imageUrl,
                    UserAuthorDto.from(form.getUser(), form.isEmailVisible()),
                    form.getExpectTime(),
                    form.getQuestionCnt(),
                    form.getResponseCnt(),
                    form.getStartDate(),
                    form.getEndDate(),
                    rewardDto,
                    tagCountDtos,
                    form.getStatus().displayName(),
                    form.isTemp());
        }
    }

    @Schema(description = "설문 목록 및 cursor 정보")
    public record FormListResponseDto(@Schema(description = "설문 목록 리스트")
                                      List<FormListDto> forms,
                                      @Schema(description = "마지막 form의 cursor 정보")
                                      @JsonInclude(JsonInclude.Include.NON_NULL)
                                      FormCursorDto cursor
    ) {
        public static FormListResponseDto from(List<FormListDto> forms,
                                               FormCursorDto formCursorDto) {
            return new FormListResponseDto(forms, formCursorDto);
        }
    }

    @Schema(description = "참여한 설문 응답 조회")
    public record ParticipateSubmissionDto(
            Long formId,
            List<SubmissionDetailResponseDto> responses,
            boolean isTemp
    ) {
        public static ParticipateSubmissionDto from(Long formId,
                                                    List<SubmissionDetailResponseDto> submissionDetailDtos,
                                                    boolean isTemp) {
            return new ParticipateSubmissionDto(formId, submissionDetailDtos, isTemp);
        }
    }
}
