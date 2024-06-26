package com.formssafe.domain.submission.repository;

import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ObjectiveSubmissionRepository extends JpaRepository<ObjectiveSubmission, Long> {
    @Query(value = "SELECT os FROM ObjectiveSubmission os WHERE os.submission.id =:responseId")
    List<ObjectiveSubmission> findAllByResponseId(@Param("responseId") Long responseId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ObjectiveSubmission os WHERE os.submission.id=:submissionId")
    void deleteAllBySubmissionId(@Param("submissionId") Long submissionId);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM ObjectiveSubmission os
            WHERE os.objectiveQuestion.id = :objectiveQuestionId 
            """)
    void deleteAllPrivacyByQuestionId(@Param("objectiveQuestionId") Long objectiveQuestionId);
}
