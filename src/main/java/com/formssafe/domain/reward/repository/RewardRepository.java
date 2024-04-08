package com.formssafe.domain.reward.repository;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    void deleteByForm(Form form);
}
