package com.formssafe.domain.batch.form.service;

import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.domain.batch.form.entity.FormBatchEnd;
import com.formssafe.domain.batch.form.entity.FormBatchStart;
import com.formssafe.domain.batch.form.repository.FormBatchEndRepository;
import com.formssafe.domain.batch.form.repository.FormBatchStartRepository;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class FormBatchServiceTest {
    @Autowired
    private FormBatchService formBatchService;
    @Autowired
    private FormBatchStartRepository formBatchStartRepository;
    @Autowired
    private FormBatchEndRepository formBatchEndRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void 시작해야하는_설문을_가져와_시작한다() {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        LocalDateTime startTime = LocalDateTime.of(2024, 3, 2, 0, 0, 0);

        Form form1 = createForm(user, "test1", "detail1", startTime, FormStatus.NOT_STARTED);
        Form form2 = createForm(user, "test2", "detail2", startTime, FormStatus.NOT_STARTED);
        List<Form> forms = formRepository.saveAll(List.of(form1, form2));

        formBatchStartRepository.saveAll(List.of(FormBatchStart.builder()
                        .serviceTime(startTime)
                        .form(forms.get(0))
                        .build(),
                FormBatchStart.builder()
                        .serviceTime(startTime)
                        .form(forms.get(1))
                        .build()
        ));
        //when
        formBatchService.startForm(startTime);
        //then
        assertThat(formRepository.findAll())
                .extracting("status")
                .containsExactly(FormStatus.PROGRESS, FormStatus.PROGRESS);
    }

    @Test
    void 마감해야하는_설문을_가져와_마감한다() {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        LocalDateTime startTime = LocalDateTime.of(2024, 3, 2, 0, 0, 0);

        Form form1 = createForm(user, "test1", "detail1", startTime, FormStatus.PROGRESS);
        Form form2 = createForm(user, "test2", "detail2", startTime, FormStatus.PROGRESS);
        List<Form> forms = formRepository.saveAll(List.of(form1, form2));

        formBatchEndRepository.saveAll(List.of(FormBatchEnd.builder()
                        .serviceTime(startTime)
                        .form(forms.get(0))
                        .build(),
                FormBatchEnd.builder()
                        .serviceTime(startTime)
                        .form(forms.get(1))
                        .build()
        ));
        //when
        formBatchService.endForm(startTime);
        //then
        assertThat(formRepository.findAll())
                .extracting("status")
                .containsExactly(FormStatus.DONE, FormStatus.DONE);
    }
}