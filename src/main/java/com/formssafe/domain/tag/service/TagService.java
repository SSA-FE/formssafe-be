package com.formssafe.domain.tag.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.tag.dto.TagRequest.TagUpdateDto;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.tag.repository.FormTagRepository;
import com.formssafe.domain.tag.repository.TagRepository;
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

    @Transactional
    public void createOrUpdateTags(List<TagUpdateDto> tags, Form form) {
        List<Tag> tagList = new ArrayList<>();
        for (TagUpdateDto dto : tags) {
            String tagName = dto.name();
            tagRepository.updateCount(tagName);
            tagList.add(tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new IllegalStateException("태그가 존재하지 않습니다.: " + tagName)));
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
}
