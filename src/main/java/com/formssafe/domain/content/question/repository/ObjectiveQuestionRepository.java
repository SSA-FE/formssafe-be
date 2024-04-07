package com.formssafe.domain.content.question.repository;

import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ObjectiveQuestionRepository extends JpaRepository<ObjectiveQuestion, Long> {

    @Modifying
    @Query("delete from ObjectiveQuestion oq where oq.form = :form")
    void deleteAllByForm(@Param("form") Form form);
}
