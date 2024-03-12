package com.formssafe.domain.form.service;

import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormRequest.CreateDto;
import com.formssafe.domain.form.dto.FormResponse;
import com.formssafe.domain.form.dto.FormResponse.ListDto;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.question.dto.QuestionResponse.DetailDto;
import com.formssafe.domain.reward.dto.RewardResponse;
import com.formssafe.domain.tag.dto.TagResponse;
import com.formssafe.domain.user.dto.UserResponse;
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

    public Page<ListDto> getList(SearchDto params) {
        log.debug(params.toString());

        ListDto formListResponse1 = new ListDto(1L, "title1", "thumbnail1",
                new UserResponse.List(1L, "minji"), 10, 2, 2,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardResponse.ListDto("냉장고", "가전제품", 3),
                new TagResponse.CountDto[]{new TagResponse.CountDto(1L, "tag1", 3),
                        new TagResponse.CountDto(2L, "tag2", 3)},
                FormStatus.PROGRESS.displayName());

        ListDto formListResponse2 = new ListDto(1L, "title2", "thumbnail2",
                new UserResponse.List(2L, "hyukjin"), 5, 3, 3,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardResponse.ListDto("청소기", "가전제품", 2),
                new TagResponse.CountDto[]{new TagResponse.CountDto(2L, "tag2", 3),
                        new TagResponse.CountDto(4L, "tag4", 3)},
                FormStatus.DONE.displayName());

        return new PageImpl<>(List.of(formListResponse1, formListResponse2));
    }

    public FormResponse.DetailDto get(Long id) {
        return new FormResponse.DetailDto(id, "title1", "description1",
                new String[]{"url1", "url2", "url3"}, new UserResponse.List(1L, "author"),
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                5, true, LocalDateTime.of(2024, 3, 10, 0, 0),
                new DetailDto[]{new DetailDto(1L, "short", "title1", "description1", null, true, true)},
                new RewardResponse.ListDto("coffee", "coffee", 5),
                new TagResponse.ListDto[]{new TagResponse.ListDto(1L, "tag1"),
                }, FormStatus.DONE.displayName(), 3,
                new UserResponse.List[]{new UserResponse.List(2L, "a"), new UserResponse.List(3L, "b")});
    }

    public void create(CreateDto request) {
        log.debug(request.toString());
    }

    public void update(Long id, CreateDto request) {
        log.debug("id: {}\n payload: {}", id, request.toString());
    }

    public void delete(Long id) {
        log.debug("id: {}", id);
    }

    public void close(Long id) {
        log.debug("id: {}", id);
    }
}
