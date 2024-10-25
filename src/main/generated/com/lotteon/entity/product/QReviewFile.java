package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewFile is a Querydsl query type for ReviewFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewFile extends EntityPathBase<ReviewFile> {

    private static final long serialVersionUID = 1313540452L;

    public static final QReviewFile reviewFile = new QReviewFile("reviewFile");

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final StringPath sname = createString("sname");

    public QReviewFile(String variable) {
        super(ReviewFile.class, forVariable(variable));
    }

    public QReviewFile(Path<? extends ReviewFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewFile(PathMetadata metadata) {
        super(ReviewFile.class, metadata);
    }

}

