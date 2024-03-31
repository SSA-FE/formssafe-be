package com.formssafe.domain.form.repository;

import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.service.SortType;
import static com.formssafe.domain.reward.entity.QReward.reward;
import static com.formssafe.domain.form.entity.QForm.form;
import static com.formssafe.domain.reward.entity.QReward.reward;
import static com.formssafe.domain.tag.entity.QFormTag.formTag;
import static com.formssafe.domain.tag.entity.QTag.tag;
import static com.formssafe.domain.user.entity.QUser.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class FormRepositoryCustomImpl implements FormRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Form> findFormAllFiltered(SearchDto searchDto) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(SortType.from(searchDto.sort()));

        return jpaQueryFactory.select(form)
                .from(form)
                .join(form.user, user).fetchJoin()
                .leftJoin(form.formTagList, formTag)
                .leftJoin(formTag.tag, tag)
                .leftJoin(form.reward, reward)
                .orderBy(orderSpecifier)
                .where(userIdLast(searchDto.top()), containsKeyword(searchDto.keyword()),
                        matchStatus(searchDto.status()),
                        containsTag(searchDto.tag()), containsCategory(searchDto.category())).fetchJoin()
                .limit(10)
                .distinct()
                .fetch();
    }

    private OrderSpecifier<?> getOrderSpecifier(SortType sortType) {
        log.info(sortType.name());
        return switch (sortType) {
            case CREATE_DATE -> form.createDate.asc();
            case END_DATE -> form.endDate.asc();
            case RESPONSE_CNT -> form.responseCnt.desc();
            default -> throw new IllegalArgumentException("Invalid sorting type: " + sortType);
        };
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword != null ? form.title.contains(keyword)
                .or(form.detail.contains(keyword)) : null;
    }

    private BooleanExpression matchStatus(String status) {
        return status != null ? form.status.eq(FormStatus.convertor(status)) : null;
    }

    private BooleanExpression containsTag(List<String> tags) {
        return tags != null && !tags.isEmpty() ? tag.tagName.in(tags) : null;
    }

    private BooleanExpression containsCategory(List<String> categories) {
        return categories != null && !categories.isEmpty() ? reward.rewardCategory.rewardCategoryName.in(categories)
                : null;
    }

    private BooleanExpression userIdLast(Long top) {
        return top != null ? form.id.gt(top) : null;
    }
}
