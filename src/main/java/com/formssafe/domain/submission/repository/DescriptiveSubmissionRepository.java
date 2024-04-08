package com.formssafe.domain.submission.repository;

import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptiveSubmissionRepository extends JpaRepository<DescriptiveSubmission, Long> {

    @Query(value = "SELECT ds FROM DescriptiveSubmission ds WHERE ds.submission.id =:responseId")
    List<DescriptiveSubmission> findAllByResponseId(@Param("responseId") Long responseId);
}
