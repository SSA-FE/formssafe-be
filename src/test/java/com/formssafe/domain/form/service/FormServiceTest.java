package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createFormWithEndDate;
import static com.formssafe.util.Fixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import com.formssafe.global.exception.type.ForbiddenException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class FormServiceTest extends IntegrationTestConfig {
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final FormService formService;

    private User testUser;

    @Autowired
    public FormServiceTest(UserRepository userRepository,
                           FormRepository formRepository,
                           FormService formService) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.formService = formService;
    }

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(createUser("testUser"));
    }

    @Nested
    class 수동_설문_종료 {

        @Test
        void 수동으로_설문을_종료한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUser = new LoginUserDto(testUser.getId());
            //when
            formService.close(form.getId(), loginUser);
            //then
            assertThat(form.getStatus()).isEqualTo(FormStatus.DONE);
        }

        @Test
        void 로그인유저와_설문작성자가_다르다면_예외가_발생한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUser = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.close(form.getId(), loginUser))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        void 설문이_존재하지_않는다면_예외가_발생한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUser = new LoginUserDto(10L);
            //when then
            assertThatThrownBy(() -> formService.close(form.getId() + 1, loginUser))
                    .isInstanceOf(DataNotFoundException.class);
        }

        @EnumSource(value = FormStatus.class, names = {"NOT_STARTED", "DONE", "REWARDED"})
        @ParameterizedTest
        void 설문이_진행중이_아니라면_예외가_발생한다(FormStatus status) {
            //given
            LocalDateTime endDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
            Form form = formRepository.save(
                    createFormWithEndDate(testUser, "설문1", "설문설명1", endDate, status));
            LoginUserDto loginUser = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.close(form.getId(), loginUser))
                    .isInstanceOf(BadRequestException.class);
        }
    }
}