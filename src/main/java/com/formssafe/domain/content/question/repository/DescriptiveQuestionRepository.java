package com.formssafe.domain.content.question.repository;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptiveQuestionRepository extends JpaRepository<DescriptiveQuestion, Long> {
}
