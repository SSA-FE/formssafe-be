package com.formssafe.domain.form.service;

import com.formssafe.domain.batch.form.service.FormBatchService;
import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.content.service.ContentService;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.service.RewardService;
import com.formssafe.domain.tag.service.TagService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import com.formssafe.global.exception.type.ForbiddenException;
import java.time.LocalDateTime;
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
public class TempFormUpdateService {
    private final FormRepository formRepository;
    private final TagService tagService;
    private final ContentService contentService;
    private final RewardService rewardService;
    private final FormBatchService formBatchService;
    private final UserRepository userRepository;

    @Transactional
    public void execute(Long formId, FormCreateDto request, LoginUserDto loginUser) {
        log.debug("TempFormUpdateService.execute: \nrequest {}\n loginUser {}", request, loginUser);

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new DataNotFoundException("해당 설문이 존재하지 않습니다.: " + formId));

        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));
        
        if (user.isDeleted()) {
            throw new DataNotFoundException("해당 유저를 찾을 수 없습니다.:" + loginUser.id());
        }

        if (!Objects.equals(form.getUser().getId(), loginUser.id())) {
            throw new ForbiddenException("userId-" + loginUser.id() + ": 설문 작성자가 아닙니다.: " + form.getUser().getId());
        }

        if (!form.isTemp()) {
            throw new BadRequestException("임시 설문만 수정할 수 있습니다.:" + form.getId());
        }

        LocalDateTime startDate = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime endDate = request.endDate() == null ? null : request.endDate().withSecond(0).withNano(0);
        log.info("startDate: {}, endDate: {}", startDate, endDate);

        int questionCnt = getQuestionCnt(request.contents());
        validate(request, startDate, endDate, questionCnt);

        clearFormRelatedData(form);
        update(request, form, questionCnt, startDate);
        registerFormEndBatch(request, endDate, form);
    }

    private int getQuestionCnt(List<ContentCreateDto> questions) {
        return (int) questions.stream()
                .filter(q -> !DecorationType.exists(q.type()))
                .count();
    }

    private void validate(FormCreateDto request, LocalDateTime now, LocalDateTime endDate, int questionCnt) {
        if (endDate != null && !now.plusMinutes(5L).isBefore(endDate)) {
            throw new BadRequestException("자동 마감 시각은 현재 시각 5분 후부터 설정할 수 있습니다.: " + endDate);
        }

        if (endDate != null && request.privacyDisposalDate() != null && endDate.isBefore(
                request.privacyDisposalDate())) {
            throw new BadRequestException("개인 정보 폐기 시각은 마감 시각 후여야 합니다.");
        }

        if (!request.isTemp() && questionCnt == 0) {
            throw new BadRequestException("설문에는 하나 이상의 설문 문항이 포함되어야 합니다.");
        }
    }

    private void clearFormRelatedData(Form form) {
        contentService.deleteContents(form);
        tagService.decreaseCount(form);
        if (form.getReward() != null) {
            rewardService.deleteReward(form);
        }
    }

    private void update(FormCreateDto request, Form form, int questionCnt, LocalDateTime startDate) {
        updateTempForm(request, form, questionCnt, startDate);

        contentService.createContents(request.contents(), form);
        tagService.createOrUpdateTags(request.tags(), form);
        if (request.reward() != null) {
            rewardService.createReward(request.reward(), form);
        }
    }

    private void updateTempForm(FormCreateDto request, Form form, int questionCnt, LocalDateTime startDate) {
        FormStatus status = FormStatus.PROGRESS;
        if (request.isTemp()) {
            status = FormStatus.NOT_STARTED;
        }

        form.updateTempForm(request, startDate, status, questionCnt);
    }

    private void registerFormEndBatch(FormCreateDto request, LocalDateTime endDate, Form form) {
        if (!request.isTemp() && endDate != null) {
            formBatchService.registerEndForm(endDate, form);
        }
    }
}
