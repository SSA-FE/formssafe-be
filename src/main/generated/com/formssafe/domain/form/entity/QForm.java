package com.formssafe.domain.form.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QForm is a Querydsl query type for Form
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QForm extends EntityPathBase<Form> {

    private static final long serialVersionUID = -1760908048L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QForm form = new QForm("form");

    public final com.formssafe.global.entity.QBaseTimeEntity _super = new com.formssafe.global.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final ListPath<com.formssafe.domain.question.entity.DescriptiveQuestion, com.formssafe.domain.question.entity.QDescriptiveQuestion> descriptiveQuestionList = this.<com.formssafe.domain.question.entity.DescriptiveQuestion, com.formssafe.domain.question.entity.QDescriptiveQuestion>createList("descriptiveQuestionList", com.formssafe.domain.question.entity.DescriptiveQuestion.class, com.formssafe.domain.question.entity.QDescriptiveQuestion.class, PathInits.DIRECT2);

    public final StringPath detail = createString("detail");

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> expectTime = createNumber("expectTime", Integer.class);

    public final ListPath<com.formssafe.domain.tag.entity.FormTag, com.formssafe.domain.tag.entity.QFormTag> formTagList = this.<com.formssafe.domain.tag.entity.FormTag, com.formssafe.domain.tag.entity.QFormTag>createList("formTagList", com.formssafe.domain.tag.entity.FormTag.class, com.formssafe.domain.tag.entity.QFormTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isEmailVisible = createBoolean("isEmailVisible");

    public final BooleanPath isTemp = createBoolean("isTemp");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final ListPath<com.formssafe.domain.question.entity.ObjectiveQuestion, com.formssafe.domain.question.entity.QObjectiveQuestion> objectiveQuestionList = this.<com.formssafe.domain.question.entity.ObjectiveQuestion, com.formssafe.domain.question.entity.QObjectiveQuestion>createList("objectiveQuestionList", com.formssafe.domain.question.entity.ObjectiveQuestion.class, com.formssafe.domain.question.entity.QObjectiveQuestion.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> privacyDisposalDate = createDateTime("privacyDisposalDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> responseCnt = createNumber("responseCnt", Integer.class);

    public final com.formssafe.domain.reward.entity.QReward reward;

    public final ListPath<com.formssafe.domain.reward.entity.RewardRecipient, com.formssafe.domain.reward.entity.QRewardRecipient> rewardRecipientList = this.<com.formssafe.domain.reward.entity.RewardRecipient, com.formssafe.domain.reward.entity.QRewardRecipient>createList("rewardRecipientList", com.formssafe.domain.reward.entity.RewardRecipient.class, com.formssafe.domain.reward.entity.QRewardRecipient.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final EnumPath<FormStatus> status = createEnum("status", FormStatus.class);

    public final StringPath title = createString("title");

    public final com.formssafe.domain.user.entity.QUser user;

    public QForm(String variable) {
        this(Form.class, forVariable(variable), INITS);
    }

    public QForm(Path<? extends Form> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QForm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QForm(PathMetadata metadata, PathInits inits) {
        this(Form.class, metadata, inits);
    }

    public QForm(Class<? extends Form> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reward = inits.isInitialized("reward") ? new com.formssafe.domain.reward.entity.QReward(forProperty("reward"), inits.get("reward")) : null;
        this.user = inits.isInitialized("user") ? new com.formssafe.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

