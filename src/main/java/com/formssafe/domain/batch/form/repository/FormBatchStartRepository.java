package com.formssafe.domain.batch.form.repository;

import com.formssafe.domain.batch.form.entity.FormBatchStart;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FormBatchStartRepository extends JpaRepository<FormBatchStart, Long> {

    @Query("""
            SELECT fbs FROM FormBatchStart fbs
                JOIN FETCH fbs.form f
                LEFT JOIN FETCH f.reward
            where fbs.serviceTime = :time
            """)
    List<FormBatchStart> findByServiceTime(LocalDateTime time);
}
