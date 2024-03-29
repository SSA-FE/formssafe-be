package com.formssafe.domain.tag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFormTag is a Querydsl query type for FormTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFormTag extends EntityPathBase<FormTag> {

    private static final long serialVersionUID = 1783712298L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFormTag formTag = new QFormTag("formTag");

    public final com.formssafe.domain.form.entity.QForm form;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTag tag;

    public QFormTag(String variable) {
        this(FormTag.class, forVariable(variable), INITS);
    }

    public QFormTag(Path<? extends FormTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFormTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFormTag(PathMetadata metadata, PathInits inits) {
        this(FormTag.class, metadata, inits);
    }

    public QFormTag(Class<? extends FormTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.form = inits.isInitialized("form") ? new com.formssafe.domain.form.entity.QForm(forProperty("form"), inits.get("form")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag")) : null;
    }

}

