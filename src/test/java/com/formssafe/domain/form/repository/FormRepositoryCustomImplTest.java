package com.formssafe.domain.form.repository;

import static com.formssafe.global.constants.FormConstants.PAGE_SIZE;
import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class FormRepositoryCustomImplTest extends IntegrationTestConfig {

    @Autowired
    private FormRepository formRepository;
    @Autowired
    private RewardCategoryRepository rewardCategoryRepository;
    @Autowired
    private UserRepository userRepository;

    static Stream<Arguments> getTagList() {
        return Stream.of(
                Arguments.of(List.of("tag1")),
                Arguments.of(List.of("tag2", "tag3", "tag5"))
        );
    }

    static Stream<Arguments> getCategoryList() {
        return Stream.of(
                Arguments.of(List.of("커피")),
                Arguments.of(List.of("커피", "상품권"))
        );
    }

    @BeforeEach
    void setUp() {
        rewardCategoryRepository.findAll();
    }

    @ValueSource(strings = {"1", "2", "3"})
    @ParameterizedTest
    void 제목또는설명에_키워드가포함된_설문목록을_조회한다(String keyword) {
        //given
        SearchDto searchDto = new SearchDto(keyword, null, null, null, null, null);
        //when
        List<Form> formAllFiltered = formRepository.findFormWithFiltered(searchDto);
        //then
        for (Form f : formAllFiltered) {
            assertThat(f.getTitle().contains(keyword) || f.getDetail().contains(keyword)).isTrue();
        }
    }

    @EnumSource(FormStatus.class)
    @ParameterizedTest
    void 특정상태의_설문목록을_조회한다(FormStatus status) {
        //given
        SearchDto searchDto = new SearchDto(null, null, null, status.displayName(), null, null);
        //when
        List<Form> formAllFiltered = formRepository.findFormWithFiltered(searchDto);
        //then
        for (Form f : formAllFiltered) {
            assertThat(f.getStatus()).isEqualTo(status);
        }
    }

    @ParameterizedTest
    @MethodSource("getTagList")
    void 특정태그들을_가지는_설문목록을_조회한다(List<String> tagList) {
        //given
        SearchDto searchDto = new SearchDto(null, null, null, null, tagList, null);
        //when
        List<Form> result = formRepository.findFormWithFiltered(searchDto);
        //then
        for (Form f : result) {
            assertThat(f.getFormTagList().stream()
                    .map(FormTag::getTag)
                    .map(Tag::getTagName)
                    .toList())
                    .containsAnyElementsOf(tagList);
        }
    }

    @ParameterizedTest
    @MethodSource("getCategoryList")
    void 특정카테고리의_경품들을_가지는_설문목록을_조회한다(List<String> categoryList) {
        //given
        SearchDto searchDto = new SearchDto(null, null, categoryList, null, null, null);
        //when
        List<Form> result = formRepository.findFormWithFiltered(searchDto);
        //then
        for (Form f : result) {
            assertThat(f.getReward().getRewardCategory().getRewardCategoryName()).isIn(categoryList);
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {4})
    void 특정아이디이후의_설문목록을_조회한다(long topOffset) {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        LocalDateTime startTime = LocalDateTime.of(2024, 3, 2, 0, 0, 0);
        List<Form> formList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            formList.add(createForm(user, "test" + i, "detail" + i, startTime));
            startTime = startTime.plusDays(1);
        }
        List<Form> forms = formRepository.saveAll(formList);

        List<Long> expected = new ArrayList<>();
        for (int i = (int) topOffset; i < topOffset + PAGE_SIZE; i++) {
            expected.add(forms.get(i).getId());
        }

        long top = forms.get(0).getId() + topOffset - 1;

        SearchDto searchDto = new SearchDto(null, null, null, null, null, top);

        //when
        List<Form> result = formRepository.findFormWithFiltered(searchDto);
        //then
        assertThat(result).hasSize(PAGE_SIZE)
                .extracting("id")
                .containsExactlyElementsOf(expected);
    }
}