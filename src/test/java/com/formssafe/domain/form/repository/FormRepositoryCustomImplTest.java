package com.formssafe.domain.form.repository;

import static com.formssafe.global.constants.FormConstants.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.config.QueryDslConfig;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
class FormRepositoryCustomImplTest {

    @Autowired
    private FormRepository formRepository;
    @Autowired
    private RewardCategoryRepository rewardCategoryRepository;

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
    @ValueSource(longs = {0, 4})
    void 특정아이디이후의_설문목록을_조회한다(long top) {
        //given
        SearchDto searchDto = new SearchDto(null, null, null, null, null, top);
        List<Long> expected = new ArrayList<>();
        for (long i = top + 1; i <= top + PAGE_SIZE; i++) {
            expected.add(i);
        }
        //when
        List<Form> result = formRepository.findFormWithFiltered(searchDto);
        //then
        assertThat(result).hasSize(PAGE_SIZE)
                .extracting("id")
                .containsExactlyElementsOf(expected);
    }
}