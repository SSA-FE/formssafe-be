package com.formssafe.domain.question.repository;

import com.formssafe.domain.question.entity.DescriptiveQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptiveQuestionRepository extends JpaRepository<DescriptiveQuestion, Long> {
}
