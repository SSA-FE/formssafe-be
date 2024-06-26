package com.formssafe.domain.view.service;

import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormResponse.FormCursorDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.dto.FormResponse.FormListResponseDto;
import com.formssafe.domain.form.dto.FormResponse.FormWithQuestionDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.service.FormReadService;
import com.formssafe.domain.form.service.FormResponseMapper;
import com.formssafe.domain.form.service.FormValidateService;
import com.formssafe.domain.form.service.SortType;
import com.formssafe.domain.hotform.service.HotFormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ViewService {
    private final FormReadService formReadService;
    private final FormValidateService formValidateService;
    private final FormResponseMapper formResponseMapper;
    private final HotFormService hotFormService;

    public FormListResponseDto getFormList(SearchDto searchDto) {
        log.info(searchDto.toString());

        List<Form> forms = formReadService.findFormListWithFilter(searchDto);

        return FormListResponseDto.from(forms.stream()
                .map(FormListDto::from)
                .toList(), FormCursorDto.from(SortType.from(searchDto.sort()),
                !forms.isEmpty() ? forms.get(forms.size() - 1) : null));
    }

    public FormWithQuestionDto getFormWithQuestion(Long formId) {
        Form form = formReadService.findFormWithUserAndTag(formId);
        formValidateService.validFormProgress(form);

        List<Content> contentList = formReadService.getContentList(formId);

        return FormWithQuestionDto.from(form,
                formResponseMapper.toContentResponseDto(contentList),
                formResponseMapper.toTagListDto(form.getFormTagList()),
                formResponseMapper.toRewardDto(form.getReward()));
    }

    public List<FormListDto> getTop10HotFormList() {
        List<Form> forms = hotFormService.getHotForms();
        return forms.stream()
                .map(FormListDto::from)
                .toList();
    }
}
