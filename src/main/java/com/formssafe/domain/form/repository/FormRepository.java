package com.formssafe.domain.form.repository;

import com.formssafe.domain.form.entity.Form;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormRepository extends JpaRepository<Form, Long>, FormRepositoryCustom {

    @Query("""
            SELECT f FROM Form f WHERE f.id = :id and f.isDeleted = false
            """)
    Optional<Form> findById(@Param("id") Long id);

    @Query(value = """
            select uuid, question_type, title, detail, null as options, is_privacy, is_required, position from descriptive_question where form_id = :id
            union
            select uuid, question_type, title, detail, question_option, is_privacy, is_required, position from objective_question where form_id = :id
            union
            select uuid, type, null, detail, null, null, null, position from decoration where form_id = :id
            """, nativeQuery = true)
    List<Object[]> findContentsById(@Param("id") Long id);

    @Query(value = """
            select f
            FROM Form f
            JOIN FETCH f.user
            LEFT JOIN FETCH f.formTagList ft
            JOIN FETCH ft.tag
            WHERE f.id = :id and f.isDeleted = false
            """)
    Optional<Form> findFormWithUserAndTag(@Param("id") Long id);

    @Modifying
    @Query("""
            UPDATE Form f SET f.isDeleted = true where f.user.id = :userId
            """)
    void deleteFormByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT f FROM Form f where f.isDeleted = false and f.endDate = :endDate
            """)
    List<Form> findAllByEndDate(@Param("endDate") LocalDateTime endDate);
}
