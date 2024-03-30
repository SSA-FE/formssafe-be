package com.formssafe.domain.form.repository;

import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.service.SortType;
import static com.formssafe.domain.reward.entity.QReward.reward;
import static com.formssafe.domain.form.entity.QForm.form;
import static com.formssafe.domain.tag.entity.QFormTag.formTag;
import static com.formssafe.domain.tag.entity.QTag.tag;
import static com.formssafe.domain.user.entity.QUser.user;
import static com.formssafe.global.util.CommonUtil.PAGE_SIZE;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class FormRepositoryCustomImpl implements FormRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<FormListDto> findFormAllFiltered(SearchDto searchDto) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(searchDto.sortTypeConvertToEnum(searchDto.sort()));

        List<Form> formsList = jpaQueryFactory.select(form)
                .from(form)
//                .innerJoin(user).on()
                .leftJoin(formTag).on(form.id.eq(formTag.form.id))
                .leftJoin(tag).on(formTag.tag.id.eq(tag.id))
                .leftJoin(reward).on(form.id.eq(reward.form.id))
                .orderBy(orderSpecifier)
                .where(userIdLast(searchDto.top()), containsKeyword(searchDto.keyword()), matchStatus(searchDto.status()),
                        containsTag(searchDto.tag()), containsCategory(searchDto.category()))
                .limit(PAGE_SIZE)
                .fetch();
        return formsList.stream()
                .map(FormListDto::from)
                .toList();
//        return new PageImpl<>(formsList.stream()
//                .map(FormListDto::from)
//                .toList());
    }

    private OrderSpecifier<?> getOrderSpecifier(SortType sortType) {
        log.info(sortType.name());
        switch (sortType) {
            case CREATE_DATE:
                return form.createDate.asc();
            case END_DATE:
                return form.endDate.asc();
            case RESPONSE_CNT:
                return form.responseCnt.desc();
            default:
                throw new IllegalArgumentException("Invalid sorting type: " + sortType);
        }
    }
    private BooleanExpression containsKeyword(String keyword){
        return keyword!=null ? form.title.contains(keyword)
                .or(form.detail.contains(keyword)) : null;
    }

    private BooleanExpression matchStatus(String status){
        return status!=null ? form.status.eq(FormStatus.convertor(status)): null;
    }

    private BooleanExpression containsTag(List<String> tags){
        return tags!=null && !tags.isEmpty() ? tag.tagName.in(tags) : null;
    }

    private BooleanExpression containsCategory(List<String> categories){
        return categories!=null && !categories.isEmpty() ? reward.rewardCategory.rewardCategoryName.in(categories) : null;
    }

    private BooleanExpression userIdLast(Long top){
        return top!=null ? form.id.gt(top):null;
    }
}
