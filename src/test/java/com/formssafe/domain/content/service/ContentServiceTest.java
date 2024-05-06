package com.formssafe.domain.content.service;

import static com.formssafe.util.Fixture.createContentCreate;
import static com.formssafe.util.Fixture.createForm;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[설문 문항 생성/삭제]")
class ContentServiceTest extends IntegrationTestConfig {
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final RewardCategoryRepository rewardCategoryRepository;
    private final ContentService contentService;

    private User testUser;

    @Autowired
    public ContentServiceTest(UserRepository userRepository,
                              FormRepository formRepository,
                              RewardCategoryRepository rewardCategoryRepository,
                              ContentService contentService) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.rewardCategoryRepository = rewardCategoryRepository;
        this.contentService = contentService;
    }

    @BeforeEach
    void setUp() {
        rewardCategoryRepository.save(RewardCategory.builder().rewardCategoryName("커피").build());
        testUser = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
    }

    @DisplayName("설문 문항 제목이 1자 이상 100자 이하가 아니라면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void fail_invalidContentTitleLength(int titleLength) {
        //given
        Form form = formRepository.save(createForm(testUser, "설문", "설명"));

        String title = "a".repeat(titleLength);
        List<ContentCreateDto> contents = List.of(createContentCreate("short", title, null, null, false));
        //when then
        assertThatThrownBy(() -> contentService.createContents(contents, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_CONTENT_TITLE_LENGTH);
    }

    @DisplayName("설문 문항 설명이 2000자 이하가 아니라면 예외가 발생한다")
    @Test
    void fail_invalidContentDescriptionLength() {
        //given
        Form form = formRepository.save(createForm(testUser, "설문", "설명"));

        String description = "a".repeat(2001);
        List<ContentCreateDto> contents = List.of(createContentCreate("short", "title", description, null, false));
        //when then
        assertThatThrownBy(() -> contentService.createContents(contents, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_CONTENT_DESCRIPTION_LENGTH);
    }
}