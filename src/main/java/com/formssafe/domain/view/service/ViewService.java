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

    public FormListResponseDto getFormList(SearchDto searchDto) {
        log.info(searchDto.toString());

        List<Form> forms = formReadService.findFormListWithFilter(searchDto);

        return FormListResponseDto.from(forms.stream()
                .map(FormListDto::from)
                .toList(), FormCursorDto.from(SortType.from(searchDto.sort()), forms.get(forms.size() - 1)));
    }

    public FormWithQuestionDto getFormWithQuestion(Long formId) {
        Form form = formReadService.findFormWithUserAndTag(formId);
        formValidateService.validFormProgress(form);

        List<Content> contentList = formReadService.getContentList(formId);

        return FormWithQuestionDto.from(form,
                FormResponseMapper.toContentResponseDto(contentList),
                FormResponseMapper.toTagListDto(form.getFormTagList()),
                FormResponseMapper.toRewardDto(form.getReward()));
    }
}
