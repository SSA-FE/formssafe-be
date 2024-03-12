package com.formssafe.domain.question.dto;

public final class QuestionResponse {

    public record DetailDto(Long id,
                            String type,
                            String title,
                            String description,
                            String[] options,
                            boolean isRequired,
                            boolean isPrivacy) {
    }
}

