package com.formssafe.init;

import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RewardCategoryInitializer implements ApplicationRunner {
    private final RewardCategoryRepository rewardCategoryRepository;

    @Override
    public void run(ApplicationArguments args) {
        rewardCategoryRepository.findAll();
    }
}
