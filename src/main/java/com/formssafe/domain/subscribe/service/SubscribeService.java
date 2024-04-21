package com.formssafe.domain.subscribe.service;

import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.subscribe.dto.SubscribeRequest.RewardListDto;
import com.formssafe.domain.subscribe.dto.SubscribeResponse.CategoryListDto;
import com.formssafe.domain.subscribe.entity.Subscribe;
import com.formssafe.domain.subscribe.repository.SubscribeRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.DataNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final RewardCategoryRepository rewardCategoryRepository;
    private final UserRepository userRepository;

    public List<CategoryListDto> getRewardCategoryWithSubscribe(LoginUserDto loginUser) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        if (user.isDeleted()) {
            throw new DataNotFoundException("해당 유저를 찾을 수 없습니다.:" + loginUser.id());
        }

        return rewardCategoryRepository.getRewardCategoryWithSubscribe(loginUser.id()).stream()
                .map(CategoryListDto::fromObject)
                .collect(Collectors.toList());
    }

    @Transactional
    public void subscribeCategory(LoginUserDto loginUser, RewardListDto rewardListDto) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        if (user.isDeleted()) {
            throw new DataNotFoundException("해당 유저를 찾을 수 없습니다.:" + loginUser.id());
        }
        deleteSubscribe(user);
        createSubscribe(rewardListDto.reward(), user);
    }

    private void createSubscribe(List<Long> rewardCategoryIdList, User user) {
        for (Long rewardCategoryId : rewardCategoryIdList) {
            RewardCategory rewardCategory = rewardCategoryRepository.findById(rewardCategoryId).orElseThrow(
                    () -> new DataNotFoundException("경품 카테고리가 존재하지 않습니다.")
            );
            Subscribe subscribe = Subscribe.builder()
                    .user(user)
                    .rewardCategory(rewardCategory)
                    .build();
            subscribeRepository.save(subscribe);
        }
    }

    private void deleteSubscribe(User user) {
        subscribeRepository.deleteByUserId(user.getId());
    }
}
