package com.formssafe.domain.activity.service;

import static com.formssafe.util.Fixture.createDeletedForm;
import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createTemporaryForm;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.activity.dto.ActivityParam.SearchDto;
import com.formssafe.domain.activity.dto.ActivityResponse.FormListDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ActivityServiceTest extends IntegrationTestConfig {
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final ActivityService activityService;

    private User testUser;
    private User otherUser;

    @Autowired
    public ActivityServiceTest(UserRepository userRepository,
                               FormRepository formRepository,
                               ActivityService activityService) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.activityService = activityService;
    }

    @BeforeEach
    void setUp() {
        testUser = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
        otherUser = userRepository.findById(2L).orElseThrow(IllegalStateException::new);
    }

    @Nested
    class 내가_등록한_설문_전체_조회 {

        @Test
        void 로그인한_유저가_등록한_설문을_전체_조회한다() {
            //given
            List<Form> formList = new ArrayList<>();
            formList.add(createForm(testUser, "설문1", "설문설명1"));
            formList.add(createForm(testUser, "설문2", "설문설명2"));
            formList.add(createDeletedForm(testUser, "설문3", "설문설명3"));
            formList.add(createTemporaryForm(testUser, "설문4", "설문설명4"));
            formList.add(createTemporaryForm(otherUser, "설문5", "설문설명5"));
            formList.add(createForm(otherUser, "설문6", "설문설명6"));
            formRepository.saveAll(formList);

            LoginUserDto loginUser = new LoginUserDto(testUser.getId());
            //when
            List<FormListDto> createdFormList = activityService.getCreatedFormList(SearchDto.createNull(), loginUser);
            //then
            assertThat(createdFormList).hasSize(3)
                    .extracting("title")
                    .containsExactlyInAnyOrder("설문1", "설문2", "설문4");
        }
    }
}