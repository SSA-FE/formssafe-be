package com.formssafe.domain.tag.service;

import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createRewardCategory;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("[태그 비즈니스 레이어 테스트]")
class TagServiceTest extends IntegrationTestConfig {
    private final TagService tagService;
    private final EntityManager em;

    private User testUser;
    private RewardCategory rewardCategory;

    @Autowired
    public TagServiceTest(TagService tagService,
                          EntityManager em) {
        this.tagService = tagService;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        testUser = em.find(User.class, 1L);

        rewardCategory = createRewardCategory("test_category");
        em.persist(rewardCategory);
    }

    @Test
    void 태그_개수가_5개_이하가_아니라면_예외가_발생한다() {
        //given
        Form form = createForm(testUser, "설문1", "설명1");
        em.persist(form);
        EntityManagerUtil.flushAndClear(em);

        List<String> tags = List.of("tag1", "tag2", "tag3", "tag4", "tag5", "tag6");
        //when then
        assertThatThrownBy(() -> tagService.createOrUpdateTags(tags, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TOTAL_TAG_SIZE);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 21})
    void 태그_이름이_1자_이상_20자_이하가_아니라면_예외가_발생한다(int tagNameLength) {
        //given
        Form form = createForm(testUser, "설문1", "설명1");
        em.persist(form);
        EntityManagerUtil.flushAndClear(em);

        List<String> tags = List.of("t".repeat(tagNameLength));
        //when then
        assertThatThrownBy(() -> tagService.createOrUpdateTags(tags, form))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TAG_NAME_LENGTH);
    }
}