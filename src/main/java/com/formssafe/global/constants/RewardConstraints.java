package com.formssafe.global.constants;

public final class RewardConstraints {
    public static final int REWARD_NAME_MIN_LENGTH = 1;
    public static final int REWARD_NAME_MAX_LENGTH = 200;
    public static final int REWARD_MIN_COUNT = 1;
    public static final int REWARD_MAX_COUNT = 1_000_000;

    private RewardConstraints() {
    }

    public static boolean isValidRewardNameLength(String name) {
        return name.length() >= REWARD_NAME_MIN_LENGTH && name.length() <= REWARD_NAME_MAX_LENGTH;
    }

    public static boolean isValidRewardCount(int count) {
        return count >= REWARD_MIN_COUNT && count <= REWARD_MAX_COUNT;
    }
}
