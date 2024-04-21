package com.formssafe.domain.subscribe.repository;

import com.formssafe.domain.subscribe.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
}
