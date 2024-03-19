package com.formssafe.domain.question.repository;

import com.formssafe.domain.question.entity.ObjectiveQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectiveQuestionRepository extends JpaRepository<ObjectiveQuestion, Integer> {
}
