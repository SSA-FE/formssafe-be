package com.formssafe.domain.form.service;

import com.formssafe.domain.content.dto.ContentResponseDto;
import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.form.dto.FormResponse.FormIdDto;
import com.formssafe.domain.reward.dto.RewardResponse.RewardDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FormResponseMapper {

    public List<TagListDto> toTagListDto(List<FormTag> formTags) {
        return formTags.stream()
                .map(FormTag::getTag)
                .map(TagListDto::from)
                .toList();
    }

    public List<ContentResponseDto> toContentResponseDto(List<Content> contents) {
        return contents.stream()
                .sorted(Comparator.comparingInt(Content::getPosition))
                .map(ContentResponseDto::from)
                .toList();
    }

    public RewardDto toRewardDto(Reward reward) {
        if (reward == null) {
            return null;
        }

        return RewardDto.from(reward, reward.getRewardCategory());
    }

    public List<UserListDto> toRewardRecipientsListDto(List<RewardRecipient> rewardRecipients) {
        if (rewardRecipients == null) {
            return null;
        }

        return rewardRecipients.stream()
                .map(RewardRecipient::getUser)
                .map(UserListDto::from)
                .toList();
    }

    public FormIdDto toFormIdDto(Long formId) {
        return new FormIdDto(formId);
    }
}
