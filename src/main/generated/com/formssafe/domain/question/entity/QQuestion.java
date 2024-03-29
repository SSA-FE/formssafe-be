package com.formssafe.domain.question.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = -2056727628L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final StringPath detail = createString("detail");

    public final com.formssafe.domain.form.entity.QForm form;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPrivacy = createBoolean("isPrivacy");

    public final BooleanPath isRequired = createBoolean("isRequired");

    public final NumberPath<Integer> position = createNumber("position", Integer.class);

    public final StringPath title = createString("title");

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.form = inits.isInitialized("form") ? new com.formssafe.domain.form.entity.QForm(forProperty("form"), inits.get("form")) : null;
    }

}

