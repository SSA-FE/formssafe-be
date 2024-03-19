package com.formssafe.domain.form.repository;

import static com.formssafe.util.Fixture.createFormTag;
import static com.formssafe.util.Fixture.createTag;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.tag.repository.FormTagRepository;
import com.formssafe.domain.tag.repository.TagRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.util.JsonListConverter;
import com.formssafe.util.Fixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
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
    private EntityManager em;

    @Test
    void 이미지url_포함_설문을_저장한다() {
        //given
        User testUser = Fixture.createUser("testUser");
        User user = userRepository.save(testUser);

        List<String> images = List.of("http://localhost/url1", "http://localhost/url2");
        Form form = Fixture.createFormWithImages(user, "설문1", "설문 설명1", images);
        //when
        Form savedForm = formRepository.save(form);
        //then
        assertThat(JsonListConverter.convertToEntityAttribute(savedForm.getImageUrl()))
                .isEqualTo(images);
    }

    @Test
    void 태그포함_설문을_가져온다() {
        //given
        User user = Fixture.createUser("testUser");
        user = userRepository.save(user);

        Form form = Fixture.createForm(user, "설문1", "설문 설명1");
        form = formRepository.save(form);

        List<Tag> tagList = List.of(createTag("tag1"), createTag("tag2"));
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
        System.out.println(formResult.getTagList().get(0));
        assertThat(formResult.getTagList())
                .hasSize(2);
    }
}