package com.formssafe.domain.question.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDescriptiveQuestion is a Querydsl query type for DescriptiveQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDescriptiveQuestion extends EntityPathBase<DescriptiveQuestion> {

    private static final long serialVersionUID = 1160688708L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDescriptiveQuestion descriptiveQuestion = new QDescriptiveQuestion("descriptiveQuestion");

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

    public final EnumPath<DescriptiveQuestionType> questionType = createEnum("questionType", DescriptiveQuestionType.class);

    //inherited
    public final StringPath title;

    public QDescriptiveQuestion(String variable) {
        this(DescriptiveQuestion.class, forVariable(variable), INITS);
    }

    public QDescriptiveQuestion(Path<? extends DescriptiveQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDescriptiveQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDescriptiveQuestion(PathMetadata metadata, PathInits inits) {
        this(DescriptiveQuestion.class, metadata, inits);
    }

    public QDescriptiveQuestion(Class<? extends DescriptiveQuestion> type, PathMetadata metadata, PathInits inits) {
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

