package com.formssafe.domain.reward.service;

import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createRewardCategory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[경품 비즈니스 레이어 테스트]")
class RewardServiceTest extends IntegrationTestConfig {
    private final RewardService rewardService;
    private final EntityManager em;

    private User testUser;
    private RewardCategory rewardCategory;

    @Autowired
    public RewardServiceTest(RewardService rewardService,
                             EntityManager em) {
        this.rewardService = rewardService;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        testUser = em.find(User.class, 1L);

        rewardCategory = createRewardCategory("test_category");
        em.persist(rewardCategory);
    }

    @Test
    void 경품을_생성한다() {
        //given
        Form form = createForm(testUser, "설문1", "설명1");
        em.persist(form);
        EntityManagerUtil.flushAndClear(em);

        RewardCreateDto rewardCreateDto = new RewardCreateDto("reward",
                rewardCategory.getRewardCategoryName(), 1);
        //when
        rewardService.createReward(rewardCreateDto, form);
        //then
        EntityManagerUtil.flushAndClear(em);

        Reward reward = em.find(Form.class, form.getId()).getReward();
        assertThat(reward.getRewardName()).isEqualTo(rewardCreateDto.name());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 201})
    void 경품명이_1자_이상_200자_이하가_아니라면_예외가_발생한다(int rewardNameLength) {
        //given
        Form form = createForm(testUser, "설문1", "설명1");
        em.persist(form);
        EntityManagerUtil.flushAndClear(em);

        RewardCreateDto rewardCreateDto = new RewardCreateDto("a".repeat(rewardNameLength),
                rewardCategory.getRewardCategoryName(), 1);
        //when then
        assertThatThrownBy(() -> rewardService.createReward(rewardCreateDto, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_REWARD_NAME_LENGTH);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1_000_001})
    void 경품_수량이_1개_이상_1_000_000개_이하가_아니라면_예외가_발생한다(int count) {
        //given
        Form form = createForm(testUser, "설문1", "설명1");
        em.persist(form);
        EntityManagerUtil.flushAndClear(em);

        RewardCreateDto rewardCreateDto = new RewardCreateDto("reward",
                rewardCategory.getRewardCategoryName(), count);
        //when then
        assertThatThrownBy(() -> rewardService.createReward(rewardCreateDto, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_REWARD_COUNT);
    }
}