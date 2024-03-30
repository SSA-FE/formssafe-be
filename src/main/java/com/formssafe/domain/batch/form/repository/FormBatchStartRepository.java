package com.formssafe.domain.batch.form.repository;

import com.formssafe.domain.batch.form.entity.FormBatchStart;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormBatchStartRepository extends JpaRepository<FormBatchStart, Long> {

    List<FormBatchStart> findByServiceTime(LocalDateTime time);
}
