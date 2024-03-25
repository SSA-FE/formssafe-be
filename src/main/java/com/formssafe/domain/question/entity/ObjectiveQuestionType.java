package com.formssafe.domain.question.entity;

public enum ObjectiveQuestionType {
    SINGLE("single"),
    CHECKBOX("checkbox"),
    DROPDOWN("dropdown");

    private final String displayName;

    ObjectiveQuestionType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
