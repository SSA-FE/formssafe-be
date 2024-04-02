package com.formssafe.domain.form.repository;

import static com.formssafe.util.Fixture.createDescriptiveQuestion;
import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createFormTag;
import static com.formssafe.util.Fixture.createObjectiveQuestion;
import static com.formssafe.util.Fixture.createReward;
import static com.formssafe.util.Fixture.createRewardCategory;
import static com.formssafe.util.Fixture.createTag;
import static com.formssafe.util.Fixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.config.QueryDslConfig;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionOption;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionType;
import com.formssafe.domain.content.question.repository.DescriptiveQuestionRepository;
import com.formssafe.domain.content.question.repository.ObjectiveQuestionRepository;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.reward.repository.RewardRepository;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.tag.repository.FormTagRepository;
import com.formssafe.domain.tag.repository.TagRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.util.JsonConverter;
import com.formssafe.util.Fixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
class FormRepositoryTest {

    @Autowired
    private FormRepository formRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FormTagRepository formTagRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private RewardCategoryRepository rewardCategoryRepository;
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private DescriptiveQuestionRepository descriptiveQuestionRepository;
    @Autowired
    private ObjectiveQuestionRepository objectiveQuestionRepository;
    @Autowired
    private EntityManager em;

    @Test
    void 이미지url_포함_설문을_저장한다() {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        List<String> images = List.of("http://localhost/url1", "http://localhost/url2");
        Form form = Fixture.createFormWithImages(user, "설문1", "설문 설명1", images);
        //when
        Form savedForm = formRepository.save(form);
        //then
        assertThat(JsonConverter.toList(savedForm.getImageUrl(), String.class))
                .isEqualTo(images);
    }

    @Test
    void 태그가_존재하는_설문을_가져온다() {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        Form form = createForm(user, "설문1", "설문 설명1");
        form = formRepository.save(form);

        List<Tag> tagList = List.of(createTag("testTag1"), createTag("testTag2"));
        tagList = tagRepository.saveAll(tagList);

        List<FormTag> formTagList = List.of(createFormTag(form, tagList.get(0)),
                createFormTag(form, tagList.get(1)));
        formTagRepository.saveAll(formTagList);

        em.clear();
        //when
        Form formResult = formRepository.findById(form.getId())
                .orElseGet(() -> null);
        //then
        assertThat(formResult).isNotNull();
        assertThat(formResult.getFormTagList())
                .hasSize(2);
    }

    @Test
    void 경품이_존재하는_설문을_가져온다() {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        Form form = createForm(user, "설문1", "설문 설명1");
        form = formRepository.save(form);

        RewardCategory rewardCategory = createRewardCategory("경품카테고리1");
        rewardCategory = rewardCategoryRepository.save(rewardCategory);

        Reward reward = createReward("경품1", form, rewardCategory, 5);
        reward = rewardRepository.save(reward);

        em.clear();
        //when
        Form formResult = formRepository.findById(form.getId())
                .orElseGet(() -> null);
        //then
        assertThat(formResult).isNotNull();
        assertThat(formResult.getReward())
                .isNotNull()
                .extracting("rewardName")
                .isEqualTo("경품1");
    }

    @Test
    void 질문이_존재하는_설문을_가져온다() {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        Form form = createForm(user, "설문1", "설문 설명1");
        form = formRepository.save(form);

        DescriptiveQuestion descriptiveQuestion = createDescriptiveQuestion(form,
                DescriptiveQuestionType.LONG,
                "주관식 질문1",
                1);

        descriptiveQuestion = descriptiveQuestionRepository.save(descriptiveQuestion);

        List<ObjectiveQuestionOption> objectiveQuestionOptions = List.of(new ObjectiveQuestionOption(0, "보기1"),
                new ObjectiveQuestionOption(1, "보기2"));
        ObjectiveQuestion objectiveQuestion = createObjectiveQuestion(form, ObjectiveQuestionType.CHECKBOX, "객관식 질문1",
                2,
                objectiveQuestionOptions);
        objectiveQuestion = objectiveQuestionRepository.save(objectiveQuestion);

        em.clear();
        //when
        Form formResult = formRepository.findById(form.getId())
                .orElseGet(() -> null);
        //then
        assertThat(formResult).isNotNull();
        assertThat(formResult.getDescriptiveQuestionList())
                .isNotNull()
                .hasSize(1)
                .extracting("title")
                .containsExactly(descriptiveQuestion.getTitle());
        assertThat(formResult.getObjectiveQuestionList())
                .isNotNull()
                .hasSize(1);
        assertThat(JsonConverter.toList(formResult.getObjectiveQuestionList().get(0).getQuestionOption(),
                ObjectiveQuestionOption.class))
                .containsExactlyElementsOf(objectiveQuestionOptions);
    }
}