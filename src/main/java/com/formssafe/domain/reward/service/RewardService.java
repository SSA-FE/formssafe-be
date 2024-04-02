package com.formssafe.domain.reward.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.reward.repository.RewardRepository;
import com.formssafe.global.exception.type.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RewardService {
    private final RewardRepository rewardRepository;
    private final RewardCategoryRepository rewardCategoryRepository;

    @Transactional
    public void createReward(RewardCreateDto request, Form form) {
        RewardCategory rewardCategory = rewardCategoryRepository.findByRewardCategoryName(
                        request.category())
                .orElseThrow(() -> new BadRequestException("유효하지 않은 카테고리입니다.: " + request.category()));

        Reward reward = Reward.builder()
                .rewardName(request.name())
                .rewardCategory(rewardCategory)
                .form(form)
                .count(request.count())
                .build();

        rewardRepository.save(reward);
    }
}
