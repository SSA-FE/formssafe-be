package com.formssafe.domain.activity.service;

import com.formssafe.domain.activity.dto.ActivityParam.SearchDto;
import com.formssafe.domain.activity.dto.ActivityResponse.FormListDto;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.reward.dto.RewardResponse;
import com.formssafe.domain.tag.dto.TagResponse;
import com.formssafe.domain.user.dto.UserResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityService {

    public Page<FormListDto> getCreatedFormList(SearchDto param) {
        log.debug(param.toString());

        FormListDto formListResponse1 = new FormListDto(1L, "title1", "thumbnail1",
                new UserResponse.List(1L, "minji"), 10, 2, 2,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardResponse.ListDto("냉장고", "가전제품", 3),
                new TagResponse.CountDto[]{new TagResponse.CountDto(1L, "tag1", 3),
                        new TagResponse.CountDto(2L, "tag2", 3)},
                FormStatus.PROGRESS.displayName());

        FormListDto formListResponse2 = new FormListDto(1L, "title2", "thumbnail2",
                new UserResponse.List(2L, "hyukjin"), 5, 3, 3,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardResponse.ListDto("청소기", "가전제품", 2),
                new TagResponse.CountDto[]{new TagResponse.CountDto(2L, "tag2", 3),
                        new TagResponse.CountDto(4L, "tag4", 3)},
                FormStatus.DONE.displayName());

        return new PageImpl<>(List.of(formListResponse1, formListResponse2));
    }

    public Page<FormListDto> getParticipatedFormList(SearchDto param) {
        log.debug(param.toString());

        FormListDto formListResponse1 = new FormListDto(1L, "title1", "thumbnail1",
                new UserResponse.List(1L, "minji"), 10, 2, 2,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardResponse.ListDto("냉장고", "가전제품", 3),
                new TagResponse.CountDto[]{new TagResponse.CountDto(1L, "tag1", 3),
                        new TagResponse.CountDto(2L, "tag2", 3)},
                FormStatus.PROGRESS.displayName());

        FormListDto formListResponse2 = new FormListDto(1L, "title2", "thumbnail2",
                new UserResponse.List(2L, "hyukjin"), 5, 3, 3,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardResponse.ListDto("청소기", "가전제품", 2),
                new TagResponse.CountDto[]{new TagResponse.CountDto(2L, "tag2", 3),
                        new TagResponse.CountDto(4L, "tag4", 3)},
                FormStatus.DONE.displayName());

        return new PageImpl<>(List.of(formListResponse1, formListResponse2));
    }
}
