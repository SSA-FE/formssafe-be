package com.formssafe.domain.reward.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRewardRecipient is a Querydsl query type for RewardRecipient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRewardRecipient extends EntityPathBase<RewardRecipient> {

    private static final long serialVersionUID = -2132133901L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRewardRecipient rewardRecipient = new QRewardRecipient("rewardRecipient");

    public final com.formssafe.domain.form.entity.QForm form;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.formssafe.domain.user.entity.QUser user;

    public QRewardRecipient(String variable) {
        this(RewardRecipient.class, forVariable(variable), INITS);
    }

    public QRewardRecipient(Path<? extends RewardRecipient> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRewardRecipient(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRewardRecipient(PathMetadata metadata, PathInits inits) {
        this(RewardRecipient.class, metadata, inits);
    }

    public QRewardRecipient(Class<? extends RewardRecipient> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.form = inits.isInitialized("form") ? new com.formssafe.domain.form.entity.QForm(forProperty("form"), inits.get("form")) : null;
        this.user = inits.isInitialized("user") ? new com.formssafe.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

