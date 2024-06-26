package com.formssafe.domain.tag.repository;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.tag.entity.FormTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormTagRepository extends JpaRepository<FormTag, Long> {

    List<FormTag> findAllByForm(Form form);

    void deleteAllByForm(Form form);
}
