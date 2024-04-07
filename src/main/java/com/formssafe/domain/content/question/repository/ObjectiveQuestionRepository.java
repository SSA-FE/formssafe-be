package com.formssafe.domain.content.question.repository;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjectiveQuestionRepository extends JpaRepository<ObjectiveQuestion, Long> {
    @Query("SELECT dq FROM ObjectiveQuestion dq WHERE dq.uuid=:uuid")
    Optional<ObjectiveQuestion> findByUuid(@Param("uuid") String uuid);
}
