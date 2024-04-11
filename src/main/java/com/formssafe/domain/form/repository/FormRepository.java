package com.formssafe.domain.form.repository;

import com.formssafe.domain.form.entity.Form;
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

    @Modifying
    @Query("""
            UPDATE Form f SET f.isDeleted = true where f.user.id = :userId
            """)
    void deleteFormByUserId(@Param("userId") Long userId);
}
