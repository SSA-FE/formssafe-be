package com.formssafe.domain.content.question.repository;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DescriptiveQuestionRepository extends JpaRepository<DescriptiveQuestion, Long> {

    @Modifying
    @Query("delete from DescriptiveQuestion dq where dq.form = :form")
    void deleteAllByForm(@Param("form") Form form);
}
