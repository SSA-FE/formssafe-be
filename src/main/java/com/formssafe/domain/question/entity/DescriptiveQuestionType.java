package com.formssafe.domain.question.entity;

public enum DescriptiveQuestionType {
    SHORT("short"),
    LONG("long");

    private final String displayName;

    DescriptiveQuestionType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
