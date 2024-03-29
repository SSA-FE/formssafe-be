package com.formssafe.domain.question.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QObjectiveQuestion is a Querydsl query type for ObjectiveQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QObjectiveQuestion extends EntityPathBase<ObjectiveQuestion> {

    private static final long serialVersionUID = 1135829041L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QObjectiveQuestion objectiveQuestion = new QObjectiveQuestion("objectiveQuestion");

    public final QQuestion _super;

    //inherited
    public final StringPath detail;

    // inherited
    public final com.formssafe.domain.form.entity.QForm form;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final BooleanPath isPrivacy;

    //inherited
    public final BooleanPath isRequired;

    //inherited
    public final NumberPath<Integer> position;

    public final StringPath questionOption = createString("questionOption");

    public final EnumPath<ObjectiveQuestionType> questionType = createEnum("questionType", ObjectiveQuestionType.class);

    //inherited
    public final StringPath title;

    public QObjectiveQuestion(String variable) {
        this(ObjectiveQuestion.class, forVariable(variable), INITS);
    }

    public QObjectiveQuestion(Path<? extends ObjectiveQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QObjectiveQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QObjectiveQuestion(PathMetadata metadata, PathInits inits) {
        this(ObjectiveQuestion.class, metadata, inits);
    }

    public QObjectiveQuestion(Class<? extends ObjectiveQuestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QQuestion(type, metadata, inits);
        this.detail = _super.detail;
        this.form = _super.form;
        this.id = _super.id;
        this.isPrivacy = _super.isPrivacy;
        this.isRequired = _super.isRequired;
        this.position = _super.position;
        this.title = _super.title;
    }

}

