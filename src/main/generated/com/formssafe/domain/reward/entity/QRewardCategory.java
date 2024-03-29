package com.formssafe.domain.reward.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRewardCategory is a Querydsl query type for RewardCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRewardCategory extends EntityPathBase<RewardCategory> {

    private static final long serialVersionUID = -1707289628L;

    public static final QRewardCategory rewardCategory = new QRewardCategory("rewardCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath rewardCategoryName = createString("rewardCategoryName");

    public QRewardCategory(String variable) {
        super(RewardCategory.class, forVariable(variable));
    }

    public QRewardCategory(Path<? extends RewardCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRewardCategory(PathMetadata metadata) {
        super(RewardCategory.class, metadata);
    }

}

