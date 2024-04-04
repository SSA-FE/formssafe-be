package com.formssafe.domain.batch.form.repository;

import static com.formssafe.util.Fixture.createFormWithEndDate;
import static com.formssafe.util.Fixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.batch.form.entity.FormBatchEnd;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormBatchEndRepositoryTest extends IntegrationTestConfig {
    @Autowired
    private FormBatchEndRepository formBatchEndRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void 마감해야하는_설문을_가져온다() {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        LocalDateTime endDate = LocalDateTime.of(2024, 3, 2, 0, 0, 0);

        Form form1 = createFormWithEndDate(user, "test1", "detail1", endDate, FormStatus.PROGRESS);
        Form form2 = createFormWithEndDate(user, "test2", "detail2", endDate, FormStatus.PROGRESS);
        List<Form> forms = formRepository.saveAll(List.of(form1, form2));

        formBatchEndRepository.saveAll(List.of(FormBatchEnd.builder()
                        .serviceTime(endDate)
                        .form(forms.get(0))
                        .build(),
                FormBatchEnd.builder()
                        .serviceTime(endDate)
                        .form(forms.get(1))
                        .build()
        ));
        //when
        List<FormBatchEnd> formBatchEndList = formBatchEndRepository.findByServiceTime(endDate);
        //then
        assertThat(formBatchEndList).hasSize(2);
    }
}