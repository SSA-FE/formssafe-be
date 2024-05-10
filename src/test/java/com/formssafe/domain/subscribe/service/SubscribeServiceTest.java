package com.formssafe.domain.subscribe.service;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.subscribe.dto.SubscribeRequest.RewardListDto;
import com.formssafe.domain.subscribe.dto.SubscribeResponse.CategoryListDto;
import com.formssafe.domain.subscribe.entity.Subscribe;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.formssafe.util.Fixture.createRewardCategory;
import static com.formssafe.util.Fixture.createSubscribe;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("[경품 카테고리 조회/구독 설정]")
class SubscribeServiceTest extends IntegrationTestConfig {
    private final SubscribeService subscribeService;
    private final EntityManager em;

    private User testUser;
    private RewardCategory rewardCategory;
    private Subscribe subscribe;

    @Autowired
    public SubscribeServiceTest(final SubscribeService subscribeService, final EntityManager em) {
        this.em = em;
        this.subscribeService = subscribeService;
    }

    @BeforeEach
    void before() {
        testUser = em.find(User.class, 1L);

        rewardCategory = createRewardCategory("커피");
        em.persist(rewardCategory);

        subscribe = createSubscribe(testUser, rewardCategory);
        em.persist(subscribe);
        EntityManagerUtil.flushAndClearContext(em);
    }

    @Nested
    class 경품_카테고리_조회 {
        @Test
        void 사용자는_자신의_구독_상태를_포함한_경품_카테고리를_조회할_수_있다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());

            //when
            List<CategoryListDto> categoryList = subscribeService.getRewardCategoryWithSubscribe(loginUserDto);

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                List<CategoryListDto> coffeeCategory = categoryList.stream()
                        .filter(category -> "커피".equals(category.name()))
                        .toList();

                List<CategoryListDto> nonCoffeeCategories = categoryList.stream()
                        .filter(category -> !"커피".equals(category.name()))
                        .toList();

                softAssertions.assertThat(coffeeCategory).hasSize(1)
                        .as("구독한 카테고리는 구독상태가 true로 반환된다")
                        .isNotEmpty()
                        .extracting("isSelected")
                        .containsOnly(true);

                softAssertions.assertThat(nonCoffeeCategories)
                        .as("구독하지않은 카테고리는 구독상태가 false로 반환된다")
                        .isNotEmpty()
                        .extracting("isSelected") // id 필드 추출
                        .containsOnly(false);
            });
        }
    }

    @Nested
    class 경품_카테고리_구독_설정 {
        @Test
        void 사용자는_자신이_원하는_카테고리에_대한_구독_설정을_할_수_있다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            List<Long> subscribedCategoryId = Arrays.asList(1L, 2L, 3L);
            RewardListDto rewardListDto = new RewardListDto(subscribedCategoryId);

            //when
            subscribeService.subscribeCategory(loginUserDto, rewardListDto);

            //then
            List<CategoryListDto> categoryList = subscribeService.getRewardCategoryWithSubscribe(loginUserDto);
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(categoryList)
                        .filteredOn(category -> subscribedCategoryId.contains(category.id()))
                        .allMatch(CategoryListDto::isSelected);

                softAssertions.assertThat(categoryList)
                        .filteredOn(category -> !subscribedCategoryId.contains(category.id()))
                        .noneMatch(CategoryListDto::isSelected);
            });
        }

        @Test
        void 사용자가_입력한_카테고리명이_존재하지_않을시_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            List<Long> subscribedCategoryId = Arrays.asList(1L, 7L);
            RewardListDto rewardListDto = new RewardListDto(subscribedCategoryId);

            //when then
            assertThatThrownBy(() -> subscribeService.subscribeCategory(loginUserDto, rewardListDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.REWARD_CATEGORY_NOT_FOUND);
        }
    }
}