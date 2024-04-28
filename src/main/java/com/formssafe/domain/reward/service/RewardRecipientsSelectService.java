package com.formssafe.domain.reward.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.notification.dto.NotificationEventDto.NotRewardedNotificationEventDto;
import com.formssafe.domain.notification.dto.NotificationEventDto.RewardedNotificationEventDto;
import com.formssafe.domain.notification.event.NotRewardedNotificationEvent;
import com.formssafe.domain.notification.event.RewardedNotificationEvent;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.reward.repository.RewardRecipientRepository;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.user.entity.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class RewardRecipientsSelectService {
    private final RewardRecipientRepository rewardRecipientRepository;
    private final SubmissionRepository submissionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void execute(Form form) {
        List<User> users = submissionRepository.findAllByFormIdWithUser(form.getId()).stream()
                .map(Submission::getUser)
                .filter(u -> !u.isDeleted())
                .collect(Collectors.toList());
        int rewardSize = getRewardSize(form.getReward(), users.size());

        Collections.shuffle(users);
        List<User> rewardedUsers = users.subList(0, rewardSize);
        List<User> notRewardedUsers = users.subList(rewardSize, users.size());

        saveRewardRecipients(form, rewardSize, rewardedUsers);

        form.changeStatus(FormStatus.REWARDED);

        applicationEventPublisher.publishEvent(new RewardedNotificationEvent(
                new RewardedNotificationEventDto(List.of(form), List.of(rewardedUsers)),
                this));
        applicationEventPublisher.publishEvent(new NotRewardedNotificationEvent(
                new NotRewardedNotificationEventDto(List.of(form), List.of(notRewardedUsers)),
                this));
    }

    private int getRewardSize(Reward reward, int userSize) {
        int size = reward.getCount();
        if (size > userSize) {
            size = userSize;
        }

        return size;
    }

    private void saveRewardRecipients(Form form, int rewardSize, List<User> rewardedUsers) {
        List<RewardRecipient> rewardedRecipients = new ArrayList<>();
        for (int i = 0; i < rewardSize; ++i) {
            rewardedRecipients.add(RewardRecipient.builder()
                    .form(form)
                    .user(rewardedUsers.get(i))
                    .build());
        }
        rewardRecipientRepository.saveAll(rewardedRecipients);
    }
}
