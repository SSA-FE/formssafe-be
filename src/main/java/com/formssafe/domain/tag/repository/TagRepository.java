package com.formssafe.domain.tag.repository;

import com.formssafe.domain.tag.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Modifying
    @Query(value = """
                INSERT INTO tag (tag_name, count) VALUES (:tagName, 1)
                ON DUPLICATE KEY UPDATE count = count + 1
            """, nativeQuery = true)
    void updateCount(@Param("tagName") String tagName);

    Optional<Tag> findByTagName(String tagName);
}
