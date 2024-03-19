package com.formssafe.domain.form.service;

import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormResponse.FormDetailDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.question.dto.QuestionResponse.QuestionDetailDto;
import com.formssafe.domain.reward.dto.RewardResponse.RewardListDto;
import com.formssafe.domain.tag.dto.TagResponse.TagCountDto;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.user.dto.UserResponse.UserAuthorDto;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class FormService {

    public Page<FormListDto> getList(SearchDto params) {
        log.debug(params.toString());

        FormListDto formListResponse1Dto = new FormListDto(1L, "title1", "thumbnail1",
                new UserAuthorDto(1L, "minji"), 10, 2, 2,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardListDto("냉장고", "가전제품", 3),
                new TagCountDto[]{new TagCountDto(1L, "tag1", 3),
                        new TagCountDto(2L, "tag2", 3)},
                FormStatus.PROGRESS.displayName());

        FormListDto formListResponse2Dto = new FormListDto(1L, "title2", "thumbnail2",
                new UserAuthorDto(2L, "hyukjin"), 5, 3, 3,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardListDto("청소기", "가전제품", 2),
                new TagCountDto[]{new TagCountDto(2L, "tag2", 3),
                        new TagCountDto(4L, "tag4", 3)},
                FormStatus.DONE.displayName());

        return new PageImpl<>(List.of(formListResponse1Dto, formListResponse2Dto));
    }

    public FormDetailDto get(Long id) {

        return new FormDetailDto(id, "title1", "description1",
                new String[]{"url1", "url2", "url3"}, new UserAuthorDto(1L, "author"),
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                5, true, LocalDateTime.of(2024, 3, 10, 0, 0),
                List.of(new QuestionDetailDto(1L, "short", "title1", "description1", null, true, true)),
                new RewardListDto("coffee", "coffee", 5),
                List.of(new TagListDto(1L, "tag1")), FormStatus.DONE.displayName(), 3,
                List.of(new UserListDto(2L, "a"), new UserListDto(3L, "b")));
    }

    public void create(FormCreateDto request) {
        log.debug(request.toString());
    }

    public void update(Long id, FormCreateDto request) {
        log.debug("id: {}\n payload: {}", id, request.toString());
    }

    public void delete(Long id) {
        log.debug("id: {}", id);
    }

    public void close(Long id) {
        log.debug("id: {}", id);
    }
}
