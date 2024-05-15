package com.formssafe.domain.batch.form.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.user.entity.User;
import com.formssafe.util.EntityManagerUtil;
import com.formssafe.util.Fixture;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("[설문 배치 테스트]")
class FormBatchServiceTest extends IntegrationTestConfig {
    private final FormBatchService formBatchService;
    private final EntityManager em;

    private User testUser;

    @Autowired
    public FormBatchServiceTest(FormBatchService formBatchService,
                                EntityManager em) {
        this.formBatchService = formBatchService;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        testUser = em.find(User.class, 1L);
    }

    @Nested
    class 설문_자동_마감 {

        @Test
        void 마감해야_하는_설문을_가져와_마감한다() {
            //given
            LocalDateTime endDate = LocalDateTime.of(2024, 3, 2, 0, 0, 0);
            Form form1 = Fixture.createFormWithEndDate(testUser, "test1", "detail1", endDate, FormStatus.PROGRESS);
            Form form2 = Fixture.createFormWithEndDate(testUser, "test2", "detail2", endDate, FormStatus.PROGRESS);
            em.persist(form1);
            em.persist(form2);
            EntityManagerUtil.flushAndClear(em);
            //when
            formBatchService.endForm(endDate);
            //then
            em.createQuery("select f from Form f", Form.class)
                    .getResultList()
                    .forEach(form -> {
                        assertThat(form.getStatus()).isEqualTo(FormStatus.DONE);
                    });
        }

        @Test
        void 진행_중이_아닌_설문은_스킵하고_진행_중인_설문만_마감한다() {
            //given
            LocalDateTime endDate = LocalDateTime.of(2024, 3, 2, 0, 0, 0);
            Form form1 = Fixture.createFormWithEndDate(testUser, "test1", "detail1", endDate, FormStatus.PROGRESS);
            Form form2 = Fixture.createFormWithEndDate(testUser, "test2", "detail2", endDate, FormStatus.PROGRESS);
            Form form3 = Fixture.createFormWithEndDate(testUser, "test3", "detail2", endDate, FormStatus.DONE);
            Form form4 = Fixture.createFormWithEndDate(testUser, "test4", "detail2", endDate, FormStatus.NOT_STARTED);
            Form form5 = Fixture.createFormWithEndDate(testUser, "test5", "detail2", endDate, FormStatus.PROGRESS);
            Form form6 = Fixture.createFormWithEndDate(testUser, "test6", "detail2", endDate, FormStatus.REWARDED);
            em.persist(form1);
            em.persist(form2);
            em.persist(form3);
            em.persist(form4);
            em.persist(form5);
            em.persist(form6);
            EntityManagerUtil.flushAndClear(em);
            //when
            formBatchService.endForm(endDate);
            //then
            List<Form> resultFormList = em.createQuery("select f from Form f", Form.class)
                    .getResultList();
            assertThat(resultFormList)
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
            em.persist(form1);
            em.persist(form2);
            EntityManagerUtil.flushAndClear(em);
            //when
            formBatchService.endForm(endDate);
            //then
            List<Form> resultFormList = em.createQuery("select f from Form f", Form.class)
                    .getResultList();
            assertThat(resultFormList)
                    .extracting("status")
                    .containsExactly(FormStatus.DONE, FormStatus.PROGRESS);
        }
    }
}