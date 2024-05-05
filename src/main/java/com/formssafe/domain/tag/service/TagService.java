package com.formssafe.domain.tag.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.tag.repository.FormTagRepository;
import com.formssafe.domain.tag.repository.TagRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final FormTagRepository formTagRepository;
    private final TagValidateService tagValidateService;

    @Transactional
    public void createOrUpdateTags(List<String> tags, Form form) {
        tagValidateService.isValidTotalTagSize(tags.size());
        for (String tagName : tags) {
            tagValidateService.isValidTagNameLength(tagName);
        }

        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tags) {
            tagRepository.updateCount(tagName);
            tagList.add(tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new BadRequestException(ErrorCode.TAG_NOT_FOUND, "Tag not found: " + tagName)));
        }

        List<FormTag> formTagList = new ArrayList<>();
        for (Tag tag : tagList) {
            formTagList.add(FormTag.builder()
                    .form(form)
                    .tag(tag)
                    .build());
        }
        formTagRepository.saveAll(formTagList);
    }

    @Transactional
    public void decreaseCount(Form form) {
        tagRepository.decreaseCountByForm(form);
        formTagRepository.deleteAllByForm(form);
    }
}
