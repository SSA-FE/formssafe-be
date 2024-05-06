package com.formssafe.global.constants;

public final class TagConstraints {
    public static final int TOTAL_TAG_SIZE = 5;
    public static final int TAG_NAME_MIN_LENGTH = 1;
    public static final int TAG_NAME_MAX_LENGTH = 10;

    private TagConstraints() {
    }

    public static boolean isValidTotalTagSize(int size) {
        return 0 <= size && size <= TOTAL_TAG_SIZE;
    }

    public static boolean isValidTagNameLength(String name) {
        return name.length() >= TAG_NAME_MIN_LENGTH && name.length() <= TAG_NAME_MAX_LENGTH;
    }
}
