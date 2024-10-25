package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 658555848L;

    public static final QReview review = new QReview("review");

    public final StringPath content = createString("content");

    public final ListPath<ReviewFile, QReviewFile> pReviewFiles = this.<ReviewFile, QReviewFile>createList("pReviewFiles", ReviewFile.class, QReviewFile.class, PathInits.DIRECT2);

    public final StringPath rating = createString("rating");

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public final StringPath title = createString("title");

    public final StringPath writer = createString("writer");

    public QReview(String variable) {
        super(Review.class, forVariable(variable));
    }

    public QReview(Path<? extends Review> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReview(PathMetadata metadata) {
        super(Review.class, metadata);
    }

}

