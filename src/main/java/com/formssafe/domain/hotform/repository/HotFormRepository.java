package com.formssafe.domain.hotform.repository;

import com.formssafe.domain.hotform.entity.HotForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HotFormRepository extends JpaRepository<HotForm, Long> {
    @Query(value = """
            SELECT hf.*
            FROM hot_form hf
            JOIN form f ON hf.form_id = f.id
            WHERE hf.save_time BETWEEN :start AND :now
            ORDER BY hf.id ASC
            LIMIT 10
            """, nativeQuery = true)
    List<HotForm> getTop10HotForms(@Param("now") LocalDateTime now, @Param("start") LocalDateTime start);
}
