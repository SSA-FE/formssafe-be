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
    public void run(FormCreateDto request, LoginUserDto loginUser) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다.: " + loginUser.id()));

        validate(request);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = request.startDate();
        if (startDate == null) {
            startDate = LocalDateTime.now();
        }
        FormStatus status = startDate.isBefore(now) ? FormStatus.PROGRESS : FormStatus.NOT_STARTED;
        int questionCnt = getQuestionCnt(request.contents());

        Form form = request.toForm(user, questionCnt, startDate, status);
        form = formRepository.save(form);

        contentService.createContents(request.contents(), form);
        tagService.createOrUpdateTags(request.tags(), form);
        if (request.reward() != null) {
            rewardService.createReward(request.reward(), form);
        }

        if (!request.isTemp()) {
            if (form.getStatus().equals(FormStatus.NOT_STARTED)) {
                formBatchService.registerStartFormManually(form);
            } else {
                formBatchService.registerEndFormManually(form);
            }
        }

        // TODO: 4/2/24 privacy 배치 테이블에 저장
    }

    private void validate(FormCreateDto request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new BadRequestException("시작 시각은 마감 시각 전 시각이어야 합니다.");
        }

        if (request.privacyDisposalDate() != null && request.endDate().isBefore(request.privacyDisposalDate())) {
            throw new BadRequestException("개인 정보 폐기 시각은 마감 시각 후여야 합니다.");
        }
    }

    private int getQuestionCnt(List<ContentCreateDto> questions) {
        return (int) questions.stream()
                .filter(q -> !DecorationType.exists(q.type()))
                .count();
    }
}
