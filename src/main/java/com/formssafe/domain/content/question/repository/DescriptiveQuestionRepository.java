package com.formssafe.domain.content.question.repository;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptiveQuestionRepository extends JpaRepository<DescriptiveQuestion, Long> {
    @Query(value = "SELECT dq FROM DescriptiveQuestion dq WHERE dq.uuid=:uuid AND dq.form.id = :formId")
    Optional<DescriptiveQuestion> findByUuidAndFormId(@Param("uuid") String uuid, @Param("formId") Long formId);
}
