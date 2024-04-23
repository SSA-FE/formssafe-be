package com.formssafe.domain.form.service;

import com.formssafe.domain.content.dto.ContentResponseDto;
import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.reward.dto.RewardResponse.RewardDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import java.util.Comparator;
import java.util.List;

public class FormResponseMapper {

    private FormResponseMapper() {
    }

    public static List<TagListDto> toTagListDto(List<FormTag> formTags) {
        return formTags.stream()
                .map(FormTag::getTag)
                .map(TagListDto::from)
                .toList();
    }

    public static List<ContentResponseDto> toContentResponseDto(List<Content> contents) {
        return contents.stream()
                .sorted(Comparator.comparingInt(Content::getPosition))
                .map(ContentResponseDto::from)
                .toList();
    }

    public static RewardDto toRewardDto(Reward reward) {
        if (reward == null) {
            return null;
        }

        return RewardDto.from(reward, reward.getRewardCategory());
    }

    public static List<UserListDto> toRewardRecipientsListDto(List<RewardRecipient> rewardRecipients) {
        if (rewardRecipients == null) {
            return null;
        }

        return rewardRecipients.stream()
                .map(RewardRecipient::getUser)
                .map(UserListDto::from)
                .toList();
    }
}
