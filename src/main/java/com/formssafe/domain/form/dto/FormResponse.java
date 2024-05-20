package com.formssafe.domain.form.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formssafe.domain.content.dto.ContentResponseDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.service.SortType;
import com.formssafe.domain.reward.dto.RewardResponse.RewardDto;
import com.formssafe.domain.tag.dto.TagResponse.TagCountDto;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.user.dto.UserResponse;
import com.formssafe.domain.user.dto.UserResponse.UserAuthorDto;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import com.formssafe.global.util.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public final class FormResponse {

    private FormResponse() {
    }

    @Schema(description = "설문 결과 조회 시 설문 상세 조회 응답 DTO")
    public record FormResultDto(@Schema(description = "설문 id")
                                Long id,
                                @Schema(description = "설문 제목")
                                String title,
                                @Schema(description = "설문 설명")
                                String description,
                                @Schema(description = "설문 설명 이미지 목록")
                                List<String> image,
                                @Schema(description = "설문 등록자")
                                UserAuthorDto author,
                                @Schema(description = "설문 시작 시각")
                                LocalDateTime startDate,
                                @Schema(description = "설문 마감 시각")
                                LocalDateTime endDate,
                                @Schema(description = "설문 참여 예상 시간")
                                int expectTime,
                                @Schema(description = "개인 정보를 묻는 질문 존재 시, 개인 정보 응답 항목 삭제 시각")
                                LocalDateTime privacyDisposalDate,
                                @Schema(description = "설문 문항 목록")
                                List<ContentResponseDto> contents,
                                @Schema(description = "설문 경품")
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                RewardDto reward,
                                @Schema(description = "설문 태그 목록")
                                List<TagListDto> tags,
                                @Schema(description = "설문 상태")
                                String status,
                                @Schema(description = "설문 질문 개수")
                                int questionCnt,
                                @Schema(description = "설문 응답 개수")
                                int responseCnt,
                                @Schema(description = "설문 상태가 rewarded인 경우, 경품에 추첨된 인원 목록")
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                List<UserListDto> recipients) {

        public static FormResultDto from(Form form,
                                         List<ContentResponseDto> contents,
                                         List<TagListDto> tags,
                                         RewardDto reward,
                                         List<UserListDto> recipients) {

            return new FormResultDto(form.getId(),
                    form.getTitle(),
                    form.getDetail(),
                    JsonConverter.toList(form.getImageUrl(), String.class),
                    UserAuthorDto.from(form.getUser(), form.isEmailVisible()),
                    form.getStartDate(),
                    form.getEndDate(),
                    form.getExpectTime(),
                    form.getPrivacyDisposalDate(),
                    contents,
                    reward,
                    tags,
                    form.getStatus().displayName(),
                    form.getQuestionCnt(),
                    form.getResponseCnt(),
                    recipients);
        }
    }

    @Schema(description = "설문 문항 포함 조회 응답 DTO")
    public record FormWithQuestionDto(@Schema(description = "설문 id")
                                      Long id,
                                      @Schema(description = "설문 제목")
                                      String title,
                                      @Schema(description = "설문 설명")
                                      String description,
                                      @Schema(description = "설문 설명 이미지 목록")
                                      List<String> image,
                                      @Schema(description = "설문 등록자")
                                      UserAuthorDto author,
                                      @Schema(description = "설문 시작 시각")
                                      LocalDateTime startDate,
                                      @Schema(description = "설문 마감 시각")
                                      LocalDateTime endDate,
                                      @Schema(description = "설문 참여 예상 시간")
                                      int expectTime,
                                      @Schema(description = "개인 정보를 묻는 질문 존재 시, 개인 정보 응답 항목 삭제 시각")
                                      LocalDateTime privacyDisposalDate,
                                      @Schema(description = "설문 문항 목록")
                                      List<ContentResponseDto> contents,
                                      @Schema(description = "설문 경품")
                                      @JsonInclude(JsonInclude.Include.NON_NULL)
                                      RewardDto reward,
                                      @Schema(description = "설문 태그 목록")
                                      List<TagListDto> tags,
                                      @Schema(description = "설문 상태")
                                      String status,
                                      @Schema(description = "설문 질문 개수")
                                      int questionCnt) {

        public static FormWithQuestionDto from(Form form,
                                               List<ContentResponseDto> contents,
                                               List<TagListDto> tags,
                                               RewardDto reward) {

            return new FormWithQuestionDto(form.getId(),
                    form.getTitle(),
                    form.getDetail(),
                    JsonConverter.toList(form.getImageUrl(), String.class),
                    UserAuthorDto.from(form.getUser(), form.isEmailVisible()),
                    form.getStartDate(),
                    form.getEndDate(),
                    form.getExpectTime(),
                    form.getPrivacyDisposalDate(),
                    contents,
                    reward,
                    tags,
                    form.getStatus().displayName(),
                    form.getQuestionCnt());
        }
    }

    @Schema(description = "설문 목록 조회 응답 DTO")
    public record FormListDto(@Schema(description = "설문 id") Long id,
                              @Schema(description = "설문 제목")
                              String title,
                              @Schema(description = "설문 설명")
                              String description,
                              @Schema(description = "설문 썸네일")
                              String thumbnail,
                              @Schema(description = "설문 등록자")
                              UserResponse.UserAuthorDto author,
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
                              String status) {

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
            return new FormListDto(form.getId(), form.getTitle(), form.getDetail(), imageUrl,
                    UserAuthorDto.from(form.getUser(), form.isEmailVisible()),
                    form.getExpectTime(), form.getQuestionCnt(),
                    form.getResponseCnt(),
                    form.getStartDate(), form.getEndDate(), rewardDto, tagCountDtos,
                    form.getStatus().displayName());
        }
    }

    @Schema(description = "설문 목록 및 cursor 정보")
    public record FormListResponseDto(@Schema(description = "설문 목록 리스트")
                                      List<FormListDto> forms,
                                      @Schema(description = "마지막 form의 cursor 정보")
                                      @JsonInclude(JsonInclude.Include.NON_NULL)
                                      FormCursorDto cursor
    ) {
        public static FormListResponseDto from(List<FormListDto> forms, FormCursorDto formCursorDto) {
            return new FormListResponseDto(forms, formCursorDto);
        }
    }

    @Schema(description = "설문 cursor 정보 dto")
    public record FormCursorDto(@Schema(description = "설문 정렬 방식")
                                String sort,
                                @Schema(description = "커서 위치의 formId")
                                Long top,
                                @Schema(description = "설문 정렬 방식이 startDate일때 커서 위치")
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                LocalDateTime startDate,
                                @Schema(description = "설문 정렬 방식이 endDate일때 커서 위치")
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                LocalDateTime endDate,
                                @Schema(description = "설문 정렬 방식이 responseCnt일때 커서 위치")
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                Integer responseCnt
    ) {
        public static FormCursorDto from(SortType sortType, Form form) {
            if (form == null) {
                return null;
            }
            FormCursorDto formCursorDto = null;

            switch (sortType) {
                case START_DATE ->
                        formCursorDto = new FormCursorDto(sortType.displayName(), form.getId(), form.getStartDate(),
                                null, null);
                case END_DATE -> formCursorDto = new FormCursorDto(sortType.displayName(), form.getId(), null,
                        form.getEndDate(), null);
            }
            return formCursorDto;
        }
    }

    @Schema(description = "설문 id 정보 dto")
    public record FormIdDto(@Schema(description = "설문 id") Long formId) {

        public static FormIdDto from(Long formId) {
            return new FormIdDto(formId);
        }
    }
}

