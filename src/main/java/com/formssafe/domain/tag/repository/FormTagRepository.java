package com.formssafe.domain.tag.repository;

import com.formssafe.domain.tag.entity.FormTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormTagRepository extends JpaRepository<FormTag, Integer> {
}
