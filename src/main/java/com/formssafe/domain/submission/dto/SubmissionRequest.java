package com.formssafe.domain.submission.dto;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "설문 응답 저장", requiredProperties = {"formId", "submissions", "isTemp"})
public final class SubmissionRequest{
    private SubmissionRequest(){
    }
    public record SubmissionDetailDto(
            @Schema(description = "질문 type") String type,
            @Schema(description = "질문 ID") String questionId,
            @Schema(description = "응답, string, int, int[]가능") Object content
    ){
        public ObjectiveSubmission toObjectiveSubmission(Submission submission, ObjectiveQuestion objectiveQuestion){
            return ObjectiveSubmission.builder()
                    .submission(submission)
                    .content(content)
                    .objectiveQuestion(objectiveQuestion)
                    .build();
        }

        public DescriptiveSubmission toDescriptiveSubmission(Submission submission, DescriptiveQuestion descriptiveQuestion){
            return DescriptiveSubmission.builder()
                    .submission(submission)
                    .content(content.toString())
                    .descriptiveQuestion(descriptiveQuestion)
                    .build();
        }
    }
    public record SubmissionCreateDto(
            @Schema(description = "응답 리스트") List<SubmissionDetailDto> submissions,
            @Schema(description = "임시 저장 여부") boolean isTemp
    ){
        public Submission toSubmission(User user, Form form){
            return Submission.builder()
                    .user(user)
                    .form(form)
                    .isTemp(isTemp())
                    .build();
        }
    }

}