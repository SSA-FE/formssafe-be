package com.formssafe.domain.tag.repository;

import com.formssafe.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
