package com.formssafe.domain.question.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ObjectiveQuestionOption {
    private Long id;
    private String detail;

    public ObjectiveQuestionOption(Long id, String detail) {
        this.id = id;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ObjectiveQuestionOption{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                '}';
    }
}
