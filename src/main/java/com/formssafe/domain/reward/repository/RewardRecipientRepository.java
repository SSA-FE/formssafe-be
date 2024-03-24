package com.formssafe.domain.reward.repository;

import com.formssafe.domain.reward.entity.RewardRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRecipientRepository extends JpaRepository<RewardRecipient, Long> {
}
