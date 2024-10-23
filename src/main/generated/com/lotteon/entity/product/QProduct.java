package com.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1825934975L;

    public static final QProduct product = new QProduct("product");

    public final NumberPath<Integer> categoryId = createNumber("categoryId", Integer.class);

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final ListPath<ProductFile, QProductFile> files = this.<ProductFile, QProductFile>createList("files", ProductFile.class, QProductFile.class, PathInits.DIRECT2);

    public final BooleanPath isCoupon = createBoolean("isCoupon");

    public final BooleanPath isSaled = createBoolean("isSaled");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath productCode = createString("productCode");

    public final StringPath ProductDesc = createString("ProductDesc");

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productName = createString("productName");

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final StringPath sellerId = createString("sellerId");

    public final NumberPath<Integer> shippingFee = createNumber("shippingFee", Integer.class);

    public final NumberPath<Integer> shippingTerms = createNumber("shippingTerms", Integer.class);

    public final NumberPath<Integer> sold = createNumber("sold", Integer.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

