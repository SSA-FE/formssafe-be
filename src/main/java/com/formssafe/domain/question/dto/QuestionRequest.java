package com.formssafe.domain.question.dto;

import java.util.Arrays;

public final class QuestionRequest {

    public record CreateDto(String type,
                            String title,
                            String description,
                            String[] options,
                            boolean isRequired,
                            boolean isPrivacy) {

        @Override
        public String toString() {
            return "CreateDto{" +
                    "type='" + type + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", options=" + Arrays.toString(options) +
                    ", isRequired=" + isRequired +
                    ", isPrivacy=" + isPrivacy +
                    '}';
        }
    }
}