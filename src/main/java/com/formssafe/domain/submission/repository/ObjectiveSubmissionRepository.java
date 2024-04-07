package com.formssafe.domain.submission.repository;

import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveSubmissionRepository extends JpaRepository<ObjectiveSubmission, Long> {
}
