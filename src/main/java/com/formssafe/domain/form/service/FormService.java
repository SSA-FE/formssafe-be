package com.formssafe.domain.form.service;

import com.formssafe.domain.content.dto.ContentResponseDto;
import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormResponse.FormDetailDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.dto.RewardResponse.RewardListDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.dto.UserResponse.UserAuthorDto;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import com.formssafe.global.exception.type.ForbiddenException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormService {
    private final FormRepository formRepository;

    public List<FormListDto> getList(SearchDto searchDto) {
        log.info(searchDto.toString());
        List<Form> result = formRepository.findFormWithFiltered(searchDto);
        return result.stream()
                .map(FormListDto::from)
                .toList();
    }

    public FormDetailDto getFormDetail(Long id) {
        Form form = getForm(id);
        UserAuthorDto userAuthorDto = getAuthor(form);

        List<TagListDto> tagListDtos = getTagList(form);
        List<ContentResponseDto> contentDetailDtos = getContentList(form);
        List<UserListDto> rewardRecipientsDtos = Collections.emptyList();

        RewardListDto rewardDto = getReward(form);
        if (rewardDto != null) {
            rewardRecipientsDtos = getRewardRecipientList(form);
        }

        return FormDetailDto.from(form,
                userAuthorDto,
                contentDetailDtos,
                rewardDto,
                tagListDtos,
                rewardRecipientsDtos);
    }

    private UserAuthorDto getAuthor(Form form) {
        User author = form.getUser();
        return UserAuthorDto.from(author);
    }

    private Form getForm(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id + "번 설문이 존재하지 않습니다."));

        if (form.isDeleted()) {
            throw new DataNotFoundException(id + "번 설문이 존재하지 않습니다.");
        }

        return form;
    }

    private List<TagListDto> getTagList(Form form) {
        return form.getFormTagList().stream()
                .map(FormTag::getTag)
                .map(TagListDto::from)
                .toList();
    }

    private RewardListDto getReward(Form form) {
        Reward reward = form.getReward();
        if (reward == null) {
            return null;
        }

        return RewardListDto.from(reward, reward.getRewardCategory());
    }

    private List<ContentResponseDto> getContentList(Form form) {
        List<Content> contents = new ArrayList<>();
        contents.addAll(form.getDescriptiveQuestionList());
        contents.addAll(form.getObjectiveQuestionList());
        contents.addAll(form.getDecorationList());
        contents.sort(Comparator.comparingInt(Content::getPosition));

        return contents.stream()
                .map(ContentResponseDto::from)
                .toList();
    }

    private List<UserListDto> getRewardRecipientList(Form form) {
        if (FormStatus.REWARDED != form.getStatus()) {
            return Collections.emptyList();
        }

        return form.getRewardRecipientList().stream()
                .map(RewardRecipient::getUser)
                .map(UserListDto::from)
                .toList();
    }

    public void update(Long id, FormCreateDto request) {
        log.debug("id: {}\n payload: {}", id, request.toString());
    }

    @Transactional
    public void delete(Long id, LoginUserDto loginUser) {
        log.debug("Form Delete: id: {}, loginUser: {}", id, loginUser.id());

        Form form = formRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 설문이 존재하지 않습니다.: " + id));

        if (!Objects.equals(form.getUser().getId(), loginUser.id())) {
            throw new ForbiddenException("userId-" + loginUser.id() + ": 설문 작성자가 아닙니다.: " + form.getUser().getId());
        }

        form.delete();
    }

    @Transactional
    public void close(Long id, LoginUserDto loginUser) {
        log.debug("Form Close: id: {}, loginUser: {}", id, loginUser.id());

        Form form = formRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 설문이 존재하지 않습니다.: " + id));

        if (!Objects.equals(form.getUser().getId(), loginUser.id())) {
            throw new ForbiddenException("userId-" + loginUser.id() + ": 설문 작성자가 아닙니다.: " + form.getUser().getId());
        }

        if (!form.getStatus().equals(FormStatus.PROGRESS)) {
            throw new BadRequestException("현재 진행 중인 설문이 아닙니다.");
        }

        form.changeStatus(FormStatus.DONE);

        // TODO: 4/6/24 경품 존재 시 당첨자 선정 로직 추가 
    }
}
