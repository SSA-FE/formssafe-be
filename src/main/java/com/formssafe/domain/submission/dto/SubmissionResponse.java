package com.formssafe.domain.submission.dto;

import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import com.formssafe.global.exception.type.DtoConvertException;
import com.formssafe.global.util.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public final class SubmissionResponse {
    private SubmissionResponse() {
    }

    @Schema(description = "참여한 설문 응답 조회")
    public record SubmissionResponseDto(
            Long formId,
            List<SubmissionDetailResponseDto> responses,
            boolean isTemp
    ) {
        public static SubmissionResponseDto from(Long formId,
                                                 List<SubmissionDetailResponseDto> submissionDetailDtos,
                                                 boolean isTemp) {
            return new SubmissionResponseDto(formId, submissionDetailDtos, isTemp);
        }
    }

    public record SubmissionDetailResponseDto(
            @Schema(description = "질문 ID") String questionId,
            @Schema(description = "응답 내용") Object content
    ) {
        public static SubmissionDetailResponseDto from(Object object) {
            if (object instanceof DescriptiveSubmission ds) {
                return fromDescriptiveSubmission(ds);
            } else if (object instanceof ObjectiveSubmission os) {
                return fromObjectSubmission(os);
            }
            throw new DtoConvertException("Question 엔티티를 DTO로 변환할 수 없습니다.: " + object.getClass());
        }

        private static SubmissionDetailResponseDto fromObjectSubmission(ObjectiveSubmission submission) {
            return new SubmissionDetailResponseDto(
                    submission.getObjectiveQuestion().getUuid(),
                    JsonConverter.toObject(submission.getContent(), Object.class)
            );
        }

        private static SubmissionDetailResponseDto fromDescriptiveSubmission(DescriptiveSubmission submission) {
            return new SubmissionDetailResponseDto(
                    submission.getDescriptiveQuestion().getUuid(),
                    submission.getContent()
            );
        }
    }
}
