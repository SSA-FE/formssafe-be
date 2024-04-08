package com.formssafe.domain.form.repository;

import static com.formssafe.domain.form.entity.QForm.form;
import static com.formssafe.domain.reward.entity.QReward.reward;
import static com.formssafe.domain.submission.entity.QSubmission.submission;
import static com.formssafe.domain.tag.entity.QFormTag.formTag;
import static com.formssafe.domain.tag.entity.QTag.tag;
import static com.formssafe.domain.user.entity.QUser.user;

import com.formssafe.domain.activity.dto.ActivityParam;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.service.SortType;
import com.formssafe.domain.user.entity.User;
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

    private OrderSpecifier<?> getOrderSpecifier(SortType sortType) {
        log.info(sortType.name());
        return switch (sortType) {
            case CREATE_DATE -> form.createDate.asc();
            case END_DATE -> form.endDate.asc();
            case RESPONSE_CNT -> form.responseCnt.desc();
            default -> throw new IllegalArgumentException("Invalid sorting type: " + sortType);
        };
    }

    @Override
    public List<Form> findFormWithFiltered(SearchDto searchDto) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(SortType.from(searchDto.sort()));

        return jpaQueryFactory.select(form)
                .from(form)
                .join(form.user, user).fetchJoin()
                .leftJoin(form.formTagList, formTag)
                .leftJoin(formTag.tag, tag)
                .leftJoin(form.reward, reward)
                .orderBy(orderSpecifier)
                .where(isNotDeleted(),
                        isNotTemp(),
                        userIdLast(searchDto.top()),
                        containsKeyword(searchDto.keyword()),
                        matchStatus(searchDto.status()),
                        containsTag(searchDto.tag()),
                        containsCategory(searchDto.category()))
                .fetchJoin()
                .limit(10)
                .distinct()
                .fetch();
    }

    @Override
    public List<Form> findFormByUserWithFiltered(ActivityParam.SearchDto searchDto, User author) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(SortType.from(searchDto.sort()));

        return jpaQueryFactory.select(form)
                .from(form)
                .join(form.user, user).fetchJoin()
                .leftJoin(form.formTagList, formTag)
                .leftJoin(formTag.tag, tag)
                .leftJoin(form.reward, reward)
                .orderBy(orderSpecifier)
                .where(matchUser(author),
                        isNotDeleted(),
                        userIdLast(searchDto.top()),
                        containsKeyword(searchDto.keyword()),
                        matchStatus(searchDto.status()),
                        containsTag(searchDto.tag()),
                        containsCategory(searchDto.category()))
                .fetchJoin()
                .limit(10)
                .distinct()
                .fetch();
    }

    @Override
    public List<Form> findFormByParticipateUserWithFiltered(ActivityParam.SearchDto searchDto, User participant) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(SortType.from(searchDto.sort()));

        return jpaQueryFactory.select(form)
                .from(form)
                .join(form.user, user).fetchJoin()
                .leftJoin(form.formTagList, formTag)
                .leftJoin(formTag.tag, tag)
                .leftJoin(form.reward, reward)
                .leftJoin(form.submissionList, submission)
                .orderBy(orderSpecifier)
                .where(matchParticipant(participant),
                        isNotDeleted(),
                        userIdLast(searchDto.top()),
                        containsKeyword(searchDto.keyword()),
                        matchStatus(searchDto.status()),
                        containsTag(searchDto.tag()),
                        containsCategory(searchDto.category()))
                .fetchJoin()
                .limit(10)
                .distinct()
                .fetch();
    }

    public BooleanExpression isNotDeleted() {
        return form.isDeleted.eq(false);
    }

    public BooleanExpression isNotTemp() {
        return form.isTemp.eq(false);
    }

    private BooleanExpression matchParticipant(User user) {
        return user != null ? submission.user.eq(user) : null;
    }

    private BooleanExpression matchUser(User user) {
        return user != null ? form.user.eq(user) : null;
    }

    private BooleanExpression matchStatus(String status) {
        return status != null ? form.status.eq(FormStatus.from(status)) : null;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword != null ? form.title.contains(keyword).or(form.detail.contains(keyword)) : null;
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
