package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProdCategory is a Querydsl query type for ProdCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProdCategory extends EntityPathBase<ProdCategory> {

    private static final long serialVersionUID = -2044437434L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProdCategory prodCategory = new QProdCategory("prodCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final StringPath name = createString("name");

    public final QProdCategory parent;

    public final StringPath subcategory = createString("subcategory");

    public QProdCategory(String variable) {
        this(ProdCategory.class, forVariable(variable), INITS);
    }

    public QProdCategory(Path<? extends ProdCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProdCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProdCategory(PathMetadata metadata, PathInits inits) {
        this(ProdCategory.class, metadata, inits);
    }

    public QProdCategory(Class<? extends ProdCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QProdCategory(forProperty("parent"), inits.get("parent")) : null;
    }

}

