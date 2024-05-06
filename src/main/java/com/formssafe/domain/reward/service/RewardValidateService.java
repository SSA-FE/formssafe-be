package com.formssafe.domain.reward.service;

import com.formssafe.global.constants.RewardConstraints;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public final class RewardValidateService {

    private RewardValidateService() {
    }

    public void isValidRewardNameLength(String rewardName) {
        if (rewardName == null || !RewardConstraints.isValidRewardNameLength(rewardName)) {
            throw new BadRequestException(ErrorCode.INVALID_REWARD_NAME_LENGTH,
                    "Invalid reward name length: " + rewardName);
        }
    }

    public void isValidRewardCount(int rewardCount) {
        if (!RewardConstraints.isValidRewardCount(rewardCount)) {
            throw new BadRequestException(ErrorCode.INVALID_REWARD_COUNT, "Invalid reward count: " + rewardCount);
        }
    }
}
