package com.formssafe.domain.tag.service;

import static com.formssafe.util.Fixture.createForm;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
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

@DisplayName("[태그 생성/업데이트/삭제]")
class TagServiceTest extends IntegrationTestConfig {
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final RewardCategoryRepository rewardCategoryRepository;
    private final TagService tagService;
    private User testUser;

    @Autowired
    public TagServiceTest(UserRepository userRepository,
                          FormRepository formRepository,
                          RewardCategoryRepository rewardCategoryRepository,
                          TagService tagService) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.rewardCategoryRepository = rewardCategoryRepository;
        this.tagService = tagService;
    }

    @BeforeEach
    void setUp() {
        rewardCategoryRepository.save(RewardCategory.builder().rewardCategoryName("커피").build());
        testUser = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
    }

    @DisplayName("태그 개수가 5개 이하가 아니라면 예외가 발생한다")
    @Test
    void fail_InvalidTagSize() {
        //given
        Form form = formRepository.save(createForm(testUser, "설문", "설명"));

        List<String> tags = List.of("tag1", "tag2", "tag3", "tag4", "tag5", "tag6");
        //when then
        assertThatThrownBy(() -> tagService.createOrUpdateTags(tags, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TOTAL_TAG_SIZE);
    }

    @DisplayName("태그 이름이 1자 이상 20자 이하가 아니라면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 21})
    void fail_InvalidTagNameLength(int tagNameLength) {
        //given
        Form form = formRepository.save(createForm(testUser, "설문", "설명"));

        List<String> tags = List.of("t".repeat(tagNameLength));
        //when then
        assertThatThrownBy(() -> tagService.createOrUpdateTags(tags, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TAG_NAME_LENGTH);
    }
}