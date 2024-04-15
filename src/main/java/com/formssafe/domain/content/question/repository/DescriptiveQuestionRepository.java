package com.formssafe.domain.content.question.repository;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.form.entity.Form;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptiveQuestionRepository extends JpaRepository<DescriptiveQuestion, Long> {
    @Query(value = "SELECT dq FROM DescriptiveQuestion dq WHERE dq.uuid=:uuid AND dq.form.id = :formId")
    Optional<DescriptiveQuestion> findByUuidAndFormId(@Param("uuid") String uuid, @Param("formId") Long formId);

    @Modifying
    @Query("delete from DescriptiveQuestion dq where dq.form = :form")
    void deleteAllByForm(@Param("form") Form form);

    @Query("""
            SELECT dq.id FROM DescriptiveQuestion dq 
            JOIN dq.form f
            where f.privacyDisposalDate = :now
            AND dq.isPrivacy = true
            AND f.isDeleted = false
            """)
    List<Long> findIdByDisposalTime(@Param("now") LocalDateTime now);
}
