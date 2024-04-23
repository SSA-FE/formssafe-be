package com.formssafe.domain.batch.form.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.util.Fixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormBatchServiceTest extends IntegrationTestConfig {
    @Autowired
    private FormBatchService formBatchService;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
    }

    @Nested
    class 설문_마감 {

        @Test
        void 마감해야하는_설문을_가져와_마감한다() {
            //given
            LocalDateTime endDate = LocalDateTime.of(2024, 3, 2, 0, 0, 0);
            Form form1 = Fixture.createFormWithEndDate(testUser, "test1", "detail1", endDate, FormStatus.PROGRESS);
            Form form2 = Fixture.createFormWithEndDate(testUser, "test2", "detail2", endDate, FormStatus.PROGRESS);
            formRepository.saveAll(List.of(form1, form2));
            //when
            formBatchService.endForm(endDate);
            //then
            assertThat(formRepository.findAll())
                    .extracting("status")
                    .containsExactly(FormStatus.DONE, FormStatus.DONE);
        }

        @Test
        void 진행중이_아닌_설문은_스킵하고_진행중인_설문만_마감한다() {
            //given
            LocalDateTime endDate = LocalDateTime.of(2024, 3, 2, 0, 0, 0);
            Form form1 = Fixture.createFormWithEndDate(testUser, "test1", "detail1", endDate, FormStatus.PROGRESS);
            Form form2 = Fixture.createFormWithEndDate(testUser, "test2", "detail2", endDate, FormStatus.PROGRESS);
            Form form3 = Fixture.createFormWithEndDate(testUser, "test3", "detail2", endDate, FormStatus.DONE);
            Form form4 = Fixture.createFormWithEndDate(testUser, "test4", "detail2", endDate, FormStatus.NOT_STARTED);
            Form form5 = Fixture.createFormWithEndDate(testUser, "test5", "detail2", endDate, FormStatus.PROGRESS);
            Form form6 = Fixture.createFormWithEndDate(testUser, "test6", "detail2", endDate, FormStatus.REWARDED);
            formRepository.saveAll(List.of(form1, form2, form3, form4, form5, form6));
            //when
            formBatchService.endForm(endDate);
            //then
            assertThat(formRepository.findAll())
                    .extracting("status")
                    .containsExactly(FormStatus.DONE, FormStatus.DONE, FormStatus.DONE, FormStatus.NOT_STARTED,
                            FormStatus.DONE, FormStatus.REWARDED);
        }

        @Test
        void 삭제되지_않은_설문만_마감한다() {
            //given
            LocalDateTime endDate = LocalDateTime.of(2024, 3, 2, 0, 0, 0);
            Form form1 = Fixture.createFormWithEndDate(testUser, "test1", "detail1", endDate, FormStatus.PROGRESS);
            Form form2 = Fixture.createFormWithEndDate(testUser, "test2", "detail2", endDate, FormStatus.PROGRESS);
            form2.delete();
            formRepository.saveAll(List.of(form1, form2));
            //when
            formBatchService.endForm(endDate);
            //then
            assertThat(formRepository.findAll())
                    .extracting("status")
                    .containsExactly(FormStatus.DONE, FormStatus.PROGRESS);
        }
    }
}