package com.formssafe.domain.batch.form.repository;

import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.domain.batch.form.entity.FormBatchStart;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FormBatchStartRepositoryTest {
    @Autowired
    private FormBatchStartRepository formBatchStartRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void 시작해야하는_설문을_가져온다() {
        //given
        User testUser = createUser("testUser");
        User user = userRepository.save(testUser);

        LocalDateTime startTime = LocalDateTime.of(2024, 3, 2, 0, 0, 0);

        Form form1 = createForm(user, "test1", "detail1", startTime);
        Form form2 = createForm(user, "test2", "detail2", startTime);
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
        List<FormBatchStart> formBatchStartList = formBatchStartRepository.findByServiceTime(startTime);
        //then
        assertThat(formBatchStartList).hasSize(2);
    }
}