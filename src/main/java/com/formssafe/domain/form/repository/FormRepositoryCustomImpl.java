package com.formssafe.domain.form.repository;

import com.formssafe.domain.activity.dto.ActivityParam;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.service.SortType;
import com.formssafe.domain.user.entity.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.formssafe.domain.form.entity.QForm.form;
import static com.formssafe.domain.reward.entity.QReward.reward;
import static com.formssafe.domain.submission.entity.QSubmission.submission;
import static com.formssafe.domain.tag.entity.QFormTag.formTag;
import static com.formssafe.domain.tag.entity.QTag.tag;
import static com.formssafe.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class FormRepositoryCustomImpl implements FormRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private List<OrderSpecifier<?>> getOrderSpecifier(SortType sortType) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        log.info(sortType.name());

        switch (sortType) {
            case START_DATE -> orderSpecifiers.add(form.startDate.desc());
            case END_DATE -> orderSpecifiers.add(form.endDate.asc());
            default -> throw new IllegalArgumentException("Invalid sorting type: " + sortType);
        }
        orderSpecifiers.add(form.id.asc());
        return orderSpecifiers;
    }

    @Override
    public List<Form> findFormWithFiltered(SearchDto searchDto) {
        List<OrderSpecifier<?>> orderSpecifier = getOrderSpecifier(SortType.from(searchDto.sort()));

        return jpaQueryFactory.select(form)
                .from(form)
                .join(form.user, user).fetchJoin()
                .leftJoin(form.formTagList, formTag)
                .leftJoin(formTag.tag, tag)
                .leftJoin(form.reward, reward)
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                .where(isNotDeleted(),
                        isUserNotDeleted(),
                        isNotTemp(),
                        afterCursor(SortType.from(searchDto.sort()), searchDto),
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
        List<OrderSpecifier<?>> orderSpecifier = getOrderSpecifier(SortType.from(searchDto.sort()));

        return jpaQueryFactory.select(form)
                .from(form)
                .join(form.user, user).fetchJoin()
                .leftJoin(form.formTagList, formTag)
                .leftJoin(formTag.tag, tag)
                .leftJoin(form.reward, reward)
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                .where(matchUser(author),
                        isUserNotDeleted(),
                        isNotDeleted(),
                        matchTemp(searchDto.temp()),
                        afterCursor(SortType.from(searchDto.sort()), searchDto),
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
        List<OrderSpecifier<?>> orderSpecifier = getOrderSpecifier(SortType.from(searchDto.sort()));

        return jpaQueryFactory.select(form)
                .from(form)
                .join(form.user, user).fetchJoin()
                .leftJoin(form.formTagList, formTag)
                .leftJoin(formTag.tag, tag)
                .leftJoin(form.reward, reward)
                .leftJoin(form.submissionList, submission)
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                .where(matchParticipant(participant),
                        isNotDeleted(),
                        afterCursor(SortType.from(searchDto.sort()), searchDto),
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
    public List<Form> findTop10HotForm() {
        return jpaQueryFactory.select(form)
                .from(form)
                .join(form.user, user).fetchJoin()
                .leftJoin(form.formTagList, formTag)
                .leftJoin(formTag.tag, tag)
                .leftJoin(form.reward, reward)
                .leftJoin(form.submissionList, submission)
                .orderBy(form.responseCnt.desc())
                .where(isNotDeleted(),
                        matchStatus("progress"))
                .fetchJoin()
                .limit(10)
                .distinct()
                .fetch();
    }

    private BooleanExpression isUserNotDeleted() {
        return user.isDeleted.eq(false);
    }

    private BooleanExpression isNotDeleted() {
        return form.isDeleted.eq(false);
    }

    private BooleanExpression isNotTemp() {
        return form.isTemp.eq(false);
    }

    private BooleanExpression matchParticipant(User user) {
        return user != null ? submission.user.eq(user) : null;
    }

    private BooleanExpression matchUser(User user) {
        return user != null ? form.user.eq(user) : null;
    }

    private BooleanExpression matchTemp(Boolean isTemp) {
        return isTemp != null ? form.isTemp.eq(isTemp) : null;
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

    private BooleanExpression afterCursor(SortType sortType, SearchDto searchDto) {
        return switch (sortType) {
            case START_DATE -> searchDto.startDate() != null ?
                    (form.startDate.eq(searchDto.startDate()).and(form.id.gt(searchDto.top())))
                            .or(form.startDate.before(searchDto.startDate())) : null;
            case END_DATE -> searchDto.endDate() != null ?
                    (form.endDate.eq(searchDto.endDate()).and(form.id.gt(searchDto.top())))
                            .or(form.endDate.after(searchDto.endDate())) : null;
        };
    }

    private BooleanExpression afterCursor(SortType sortType, ActivityParam.SearchDto searchDto) {
        return switch (sortType) {
            case START_DATE -> searchDto.startDate() != null ?
                    (form.startDate.eq(searchDto.startDate()).and(form.id.gt(searchDto.top())))
                            .or(form.startDate.before(searchDto.startDate())) : null;
            case END_DATE -> searchDto.endDate() != null ?
                    (form.endDate.eq(searchDto.endDate()).and(form.id.gt(searchDto.top())))
                            .or(form.endDate.after(searchDto.endDate())) : null;
        };
    }
}
