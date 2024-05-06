package com.formssafe.global.constants;

public final class ContentConstraints {
    public static final int QUESTION_MIN_SIZE = 1;
    public static final int QUESTION_MAX_SIZE = 100;
    public static final int CONTENT_TITLE_MIN_LENGTH = 1;
    public static final int CONTENT_TITLE_MAX_LENGTH = 100;
    public static final int CONTENT_DESCRIPTION_MAX_LENGTH = 1000;

    private ContentConstraints() {
    }

    public static boolean isValidQuestionSize(int size) {
        return size >= QUESTION_MIN_SIZE && size <= QUESTION_MAX_SIZE;
    }

    public static boolean isValidContentTitleLength(String title) {
        return title.length() >= CONTENT_TITLE_MIN_LENGTH && title.length() <= CONTENT_TITLE_MAX_LENGTH;
    }

    public static boolean isValidContentDescriptionLength(String description) {
        return description.length() <= CONTENT_DESCRIPTION_MAX_LENGTH;
    }
}
