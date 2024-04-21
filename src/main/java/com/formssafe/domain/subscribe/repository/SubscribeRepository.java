package com.formssafe.domain.subscribe.repository;

import com.formssafe.domain.subscribe.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    @Modifying
    @Query("delete from Subscribe s where s.user.id = :userId")
    void deleteByUserId(@Param("userId") Long id);
}
