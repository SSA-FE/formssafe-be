package com.formssafe.domain.view.service;

import com.formssafe.domain.content.dto.ContentResponseDto;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.dto.FormResponse.FormWithQuestionResponse;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.service.FormReadService;
import com.formssafe.domain.form.service.FormValidateService;
import com.formssafe.domain.reward.dto.RewardResponse.RewardListDto;
import com.formssafe.domain.reward.entity.Reward;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ViewService {
    private final FormReadService formReadService;
    private final FormValidateService formValidateService;

    public List<FormListDto> getFormList(SearchDto searchDto) {
        log.info(searchDto.toString());

        return formReadService.findFormListWithFilter(searchDto).stream()
                .map(FormListDto::from)
                .toList();
    }

    public FormWithQuestionResponse getFormWithQuestion(Long formId) {
        Form form = formReadService.findFormWithUserAndTag(formId);
        formValidateService.validFormProgress(form);

        List<ContentResponseDto> contentDetailDtos = getContentResponseDtos(form);
        RewardListDto rewardDto = getReward(form);

        return FormWithQuestionResponse.from(form,
                contentDetailDtos,
                rewardDto);
    }

    private List<ContentResponseDto> getContentResponseDtos(Form form) {
        return formReadService.getContentList(form.getId()).stream()
                .map(ContentResponseDto::from)
                .toList();
    }

    private RewardListDto getReward(Form form) {
        Reward reward = form.getReward();
        if (reward == null) {
            return null;
        }

        return RewardListDto.from(reward, reward.getRewardCategory());
    }
}
