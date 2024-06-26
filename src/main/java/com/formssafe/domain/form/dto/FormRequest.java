package com.formssafe.domain.form.dto;

import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.global.util.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public final class FormRequest {

    private FormRequest() {
    }

    @Schema(description = "설문 등록 요청 DTO", requiredProperties = {"title"})
    public record FormCreateDto(@Schema(description = "설문 제목") String title,
                                @Schema(description = "설문 설명") String description,
                                @Schema(description = "설문 설명 이미지 목록") List<String> image,
                                @Schema(description = "설문 마감 시각") LocalDateTime endDate,
                                @Schema(description = "설문 참여 예상 시간") int expectTime,
                                @Schema(description = "개인 정보를 묻는 질문 존재 시, 개인 정보 응답 항목 삭제 시각") LocalDateTime privacyDisposalDate,
                                @Schema(description = "설문 문항 목록") List<ContentCreateDto> contents,
                                @Schema(description = "설문 태그 목록") List<String> tags,
                                @Schema(description = "설문 경품") RewardCreateDto reward,
                                @Schema(description = "설문 임시 저장 여부") boolean isTemp) {

        public FormCreateDto(@Schema(description = "설문 제목") String title,
                             @Schema(description = "설문 설명") String description,
                             @Schema(description = "설문 설명 이미지 목록") List<String> image,
                             @Schema(description = "설문 마감 시각") LocalDateTime endDate,
                             @Schema(description = "설문 참여 예상 시간") int expectTime,
                             @Schema(description = "개인 정보를 묻는 질문 존재 시, 개인 정보 응답 항목 삭제 시각") LocalDateTime privacyDisposalDate,
                             @Schema(description = "설문 문항 목록") List<ContentCreateDto> contents,
                             @Schema(description = "설문 태그 목록") List<String> tags,
                             @Schema(description = "설문 경품") RewardCreateDto reward,
                             @Schema(description = "설문 임시 저장 여부") boolean isTemp) {
            this.title = title;
            this.description = description;
            this.image = image;
            this.endDate = DateTimeUtil.truncateSecondsAndNanos(endDate);
            this.expectTime = expectTime;
            this.privacyDisposalDate = DateTimeUtil.truncateSecondsAndNanos(privacyDisposalDate);
            this.contents = contents;
            this.tags = tags;
            this.reward = reward;
            this.isTemp = isTemp;
        }

        @Override
        public String toString() {
            return "FormCreateDto{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", image="
                    + image + ", endDate=" + endDate + ", expectTime=" + expectTime + ", privacyDisposalDate="
                    + privacyDisposalDate + ", contents=" + contents
                    + ", tags=" + tags + ", reward=" + reward + ", isTemp=" + isTemp + '}';
        }
    }

    @Schema(description = "설문 수정 요청 DTO", requiredProperties = {"id", "title"})
    public record FormUpdateDto(@Schema(description = "설문 제목") String title,
                                @Schema(description = "설문 설명") String description,
                                @Schema(description = "설문 설명 이미지 목록") List<String> image,
                                @Schema(description = "설문 마감 시각") LocalDateTime endDate,
                                @Schema(description = "설문 참여 예상 시간") int expectTime,
                                @Schema(description = "작성자 이메일 공개 동의 여부") boolean emailVisibility,
                                @Schema(description = "개인 정보를 묻는 질문 존재 시, 개인 정보 응답 항목 삭제 시각") LocalDateTime privacyDisposalDate,
                                @Schema(description = "설문 문항 목록") List<ContentCreateDto> contents,
                                @Schema(description = "설문 태그 목록") List<String> tags,
                                @Schema(description = "설문 경품") RewardCreateDto reward,
                                @Schema(description = "설문 임시 저장 여부") boolean isTemp) {

        public FormUpdateDto(@Schema(description = "설문 제목") String title,
                             @Schema(description = "설문 설명") String description,
                             @Schema(description = "설문 설명 이미지 목록") List<String> image,
                             @Schema(description = "설문 마감 시각") LocalDateTime endDate,
                             @Schema(description = "설문 참여 예상 시간") int expectTime,
                             @Schema(description = "작성자 이메일 공개 동의 여부") boolean emailVisibility,
                             @Schema(description = "개인 정보를 묻는 질문 존재 시, 개인 정보 응답 항목 삭제 시각") LocalDateTime privacyDisposalDate,
                             @Schema(description = "설문 문항 목록") List<ContentCreateDto> contents,
                             @Schema(description = "설문 태그 목록") List<String> tags,
                             @Schema(description = "설문 경품") RewardCreateDto reward,
                             @Schema(description = "설문 임시 저장 여부") boolean isTemp) {
            this.title = title;
            this.description = description;
            this.image = image;
            this.endDate = DateTimeUtil.truncateSecondsAndNanos(endDate);
            this.expectTime = expectTime;
            this.emailVisibility = emailVisibility;
            this.privacyDisposalDate = DateTimeUtil.truncateSecondsAndNanos(privacyDisposalDate);
            this.contents = contents;
            this.tags = tags;
            this.reward = reward;
            this.isTemp = isTemp;
        }

        @Override
        public String toString() {
            return "FormUpdateDto{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", image="
                    + image + ", endDate=" + endDate + ", expectTime=" + expectTime + ", emailVisibility="
                    + emailVisibility + ", privacyDisposalDate=" + privacyDisposalDate + ", contents=" + contents
                    + ", tags=" + tags + ", reward=" + reward + ", isTemp=" + isTemp + '}';
        }
    }
}