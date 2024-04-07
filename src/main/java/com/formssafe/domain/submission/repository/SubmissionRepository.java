package com.formssafe.domain.submission.repository;

import com.formssafe.domain.submission.entity.Submission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    @Query("SELECT s FROM Submission s WHERE s.form.id = :formId AND s.user.id = :userId")
    Optional<Submission> findSubmissionByFormIDAndUserId(@Param("formId") Long FormId, @Param("userId") Long userId);
}
