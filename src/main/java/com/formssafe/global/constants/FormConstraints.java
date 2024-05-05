package com.formssafe.global.constants;

public final class FormConstraints {
    public static final int FORM_TITLE_MIN_LENGTH = 1;
    public static final int FORM_TITLE_MAX_LENGTH = 100;
    public static final int FORM_DESCRIPTION_MAX_LENGTH = 2000;
    public static final int TOTAL_IMAGE_MAX_SIZE = 5;
    public static final int EXPECT_MIN_TIME = 1;
    public static final int EXPECT_MAX_TIME = 1440;

    public static final int PAGE_SIZE = 10;

    private FormConstraints() {
    }

    public static boolean isValidFormTitleLength(String title) {
        return title.length() >= FORM_TITLE_MIN_LENGTH && title.length() <= FORM_TITLE_MAX_LENGTH;
    }

    public static boolean isValidFormDescriptionLength(String description) {
        return description.length() <= FORM_DESCRIPTION_MAX_LENGTH;
    }

    public static boolean isValidFormExpectTime(int expectTime) {
        return expectTime >= EXPECT_MIN_TIME && expectTime <= EXPECT_MAX_TIME;
    }

    public static boolean isValidTempFormExpectTime(int expectTime) {
        return expectTime <= EXPECT_MAX_TIME;
    }

    public static boolean isValidTotalImageSize(int totalImage) {
        return totalImage <= TOTAL_IMAGE_MAX_SIZE;
    }
}
