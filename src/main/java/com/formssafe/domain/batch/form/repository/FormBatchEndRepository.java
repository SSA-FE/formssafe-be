package com.formssafe.domain.batch.form.repository;

import com.formssafe.domain.batch.form.entity.FormBatchEnd;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormBatchEndRepository extends JpaRepository<FormBatchEnd, Long> {

    @Query("""
            SELECT fbs FROM FormBatchEnd fbs
                JOIN FETCH fbs.form f
                LEFT JOIN FETCH f.reward
            where fbs.serviceTime = :time
            """)
    List<FormBatchEnd> findByServiceTime(@Param("time") LocalDateTime time);
}
