package com.formssafe.domain.form.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.question.dto.QuestionResponse.QuestionDetailDto;
import com.formssafe.domain.reward.dto.RewardResponse.RewardListDto;
import com.formssafe.domain.tag.dto.TagResponse.TagCountDto;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.tag.entity.Tag;
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

    @Schema(description = "설문 상세 조회 응답 DTO")
    public record FormDetailDto(@Schema(description = "설문 id")
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
                                @Schema(description = "작성자 이메일 공개 동의 여부")
                                boolean emailVisibility,
                                @Schema(description = "개인 정보를 묻는 질문 존재 시, 개인 정보 응답 항목 삭제 시각")
                                LocalDateTime privacyDisposalDate,
                                @Schema(description = "설문 문항 목록")
                                List<QuestionDetailDto> questions,
                                @Schema(description = "설문 경품")
                                    @JsonInclude(JsonInclude.Include.NON_NULL)
                                RewardListDto reward,
                                @Schema(description = "설문 태그 목록")
                                List<TagListDto> tags,
                                @Schema(description = "설문 상태")
                                String status,
                                @Schema(description = "설문 응답 개수")
                                int responseCnt,
                                @Schema(description = "설문 상태가 rewarded인 경우, 경품에 추첨된 인원 목록")
                                List<UserListDto> recipients) {

        public static FormDetailDto from(Form form,
                                         UserAuthorDto authorDto,
                                         List<QuestionDetailDto> questions,
                                         RewardListDto reward,
                                         List<TagListDto> tags,
                                         List<UserListDto> recipients) {
            return new FormDetailDto(form.getId(),
                    form.getTitle(),
                    form.getDetail(),
                    JsonConverter.toList(form.getImageUrl(), String.class),
                    authorDto,
                    form.getStartDate(),
                    form.getEndDate(),
                    form.getExpectTime(),
                    form.isEmailVisible(),
                    form.getPrivacyDisposalDate(),
                    questions,
                    reward,
                    tags,
                    form.getStatus().displayName(),
                    form.getResponseCnt(),
                    recipients);
        }
    }

    @Schema(description = "설문 목록 조회 응답 DTO")
    public record FormListDto(@Schema(description = "설문 id") Long id,
                              @Schema(description = "설문 제목")
                              String title,
                              @Schema(description = "설문 썸네일")
                              String thumbnail,
                              @Schema(description = "설문 등록자")
                              UserResponse.UserAuthorDto author,
                              @Schema(description = "설문 참여 예상 시간")
                              int expectTime,
//                              @Schema(description = "설문 문항 개수")
//                              int questionCnt,
                              @Schema(description = "설문 응답 개수")
                              int responseCnt,
                              @Schema(description = "설문 시작 시각")
                              LocalDateTime startDate,
                              @Schema(description = "설문 마감 시각")
                              LocalDateTime endDate,
                              @Schema(description = "설문 참여 시 받을 수 있는 경품")
                              RewardListDto reward,
                              @Schema(description = "설문 태그 목록")
                              List<TagCountDto> tags,
                              @Schema(description = "설문 상태")
                              String status) {
        public static FormListDto from(Form form){
            String imageUrl = null;
            if(form.getImageUrl()!=null){
                imageUrl = JsonConverter.getValueFromKey(form.getImageUrl(), "url");
            }

            RewardListDto rewardListDto = null;
            if (form.getReward() != null) {
                rewardListDto = RewardListDto.from(form.getReward(), form.getReward().getRewardCategory());
            }

            List<TagCountDto> tagCountDtos = null;
            if (form.getFormTagList() != null) {
                tagCountDtos = TagCountDto.from(form.getFormTagList());
            }
            return new FormListDto(form.getId(), form.getTitle(), imageUrl,
                   UserAuthorDto.from(form.getUser()), form.getExpectTime(), form.getResponseCnt(),
                    form.getStartDate(), form.getEndDate(), rewardListDto, tagCountDtos, form.getStatus().displayName());
        }
    }
}

