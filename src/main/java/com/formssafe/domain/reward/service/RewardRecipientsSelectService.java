package com.formssafe.domain.reward.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class RewardRecipientsSelectService {
    private final RewardRecipientRepository rewardRecipientRepository;
    private final SubmissionRepository submissionRepository;

    public RewardRecipientsSelectService(RewardRecipientRepository rewardRecipientRepository,
                                         SubmissionRepository submissionRepository) {
        this.rewardRecipientRepository = rewardRecipientRepository;
        this.submissionRepository = submissionRepository;
    }

    @Transactional
    public void execute(Form form) {
        List<User> users = submissionRepository.findAllByFormIdWithUser(form.getId()).stream()
                .map(Submission::getUser)
                .filter(u -> !u.isDeleted())
                .collect(Collectors.toList());
        int rewardSize = getRewardSize(form.getReward(), users.size());

        Collections.shuffle(users);
        selectRewardRecipients(form, rewardSize, users);
        form.changeStatus(FormStatus.REWARDED);
    }

    private int getRewardSize(Reward reward, int userSize) {
        int size = reward.getCount();
        if (size > userSize) {
            size = userSize;
        }

        return size;
    }

    private void selectRewardRecipients(Form form, int rewardSize, List<User> users) {
        List<RewardRecipient> rewardedUsers = new ArrayList<>();
        for (int i = 0; i < rewardSize; ++i) {
            rewardedUsers.add(RewardRecipient.builder()
                    .form(form)
                    .user(users.get(i))
                    .build());
        }
        rewardRecipientRepository.saveAll(rewardedUsers);
    }
}
