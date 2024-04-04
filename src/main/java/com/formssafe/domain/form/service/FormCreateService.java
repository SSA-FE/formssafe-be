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
import com.formssafe.global.exception.type.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormCreateService {
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final TagService tagService;
    private final ContentService contentService;
    private final RewardService rewardService;
    private final FormBatchService formBatchService;

    @Transactional
    public void execute(FormCreateDto request, LoginUserDto loginUser) {
        log.debug("FormCreateService.execute: \nrequest {}\n loginUser {}", request, loginUser);

        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다.: " + loginUser.id()));

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime endDate = request.endDate() == null ? null : request.endDate().withSecond(0).withNano(0);
        log.info("now: {}, endDate: {}", now, endDate);

        int questionCnt = getQuestionCnt(request.contents());
        validate(request, now, endDate, questionCnt);

        Form form = createForm(request, user, questionCnt, endDate);
        contentService.createContents(request.contents(), form);
        tagService.createOrUpdateTags(request.tags(), form);
        if (request.reward() != null) {
            rewardService.createReward(request.reward(), form);
        }

        if (!request.isTemp() && endDate != null) {
            formBatchService.registerEndFormManually(endDate, form);
        }
    }

    private Form createForm(FormCreateDto request, User user, int questionCnt, LocalDateTime endDate) {
        Form form = request.toForm(user, questionCnt, endDate, FormStatus.PROGRESS);
        return formRepository.save(form);
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
}
