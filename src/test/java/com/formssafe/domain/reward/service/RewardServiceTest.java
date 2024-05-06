package com.formssafe.domain.reward.service;

import static com.formssafe.util.Fixture.createForm;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[경품 생성/삭제]")
class RewardServiceTest extends IntegrationTestConfig {
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final RewardCategoryRepository rewardCategoryRepository;
    private final RewardService rewardService;
    private User testUser;

    @Autowired
    public RewardServiceTest(UserRepository userRepository,
                             FormRepository formRepository,
                             RewardCategoryRepository rewardCategoryRepository,
                             RewardService rewardService) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.rewardCategoryRepository = rewardCategoryRepository;
        this.rewardService = rewardService;
    }

    @BeforeEach
    void setUp() {
        rewardCategoryRepository.save(RewardCategory.builder().rewardCategoryName("커피").build());
        testUser = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
    }

    @DisplayName("경품명이 1자 이상 200자 이하가 아니라면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 201})
    void fail_InvalidRewardNameLength(int rewardNameLength) {
        //given
        Form form = formRepository.save(createForm(testUser, "설문", "설명"));

        RewardCreateDto rewardCreateDto = new RewardCreateDto("a".repeat(rewardNameLength), "커피", 1);
        //when then
        assertThatThrownBy(() -> rewardService.createReward(rewardCreateDto, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_REWARD_NAME_LENGTH);
    }

    @DisplayName("경품 수량이 1개 이상 1_000_000개 이하가 아니라면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 1_000_001})
    void fail_InvalidRewardCount(int count) {
        //given
        Form form = formRepository.save(createForm(testUser, "설문", "설명"));

        RewardCreateDto rewardCreateDto = new RewardCreateDto("reward", "커피", count);
        //when then
        assertThatThrownBy(() -> rewardService.createReward(rewardCreateDto, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_REWARD_COUNT);
    }
}