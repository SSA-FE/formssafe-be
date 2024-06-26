package com.formssafe.domain.content.question.repository;

import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
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
public interface ObjectiveQuestionRepository extends JpaRepository<ObjectiveQuestion, Long> {
    @Query("SELECT dq FROM ObjectiveQuestion dq WHERE dq.uuid=:uuid AND dq.form.id = :formId")
    Optional<ObjectiveQuestion> findByUuidAndFormId(@Param("uuid") String uuid, @Param("formId") Long formId);

    @Modifying
    @Query("delete from ObjectiveQuestion oq where oq.form = :form")
    void deleteAllByForm(@Param("form") Form form);

    @Query("""
            SELECT oq.id FROM ObjectiveQuestion oq 
            JOIN oq.form f
            where f.privacyDisposalDate = :now
            AND oq.isPrivacy = true
            AND f.isDeleted = false
            """)
    List<Long> findIdByDisposalTime(@Param("now") LocalDateTime now);
}
