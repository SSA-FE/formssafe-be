package com.formssafe.domain.submission.repository;

import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptiveSubmissionRepository extends JpaRepository<DescriptiveSubmission, Long> {
}
