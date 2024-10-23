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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final NumberPath<Integer> categoryId = createNumber("categoryId", Integer.class);

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final ListPath<ProductFile, QProductFile> files = this.<ProductFile, QProductFile>createList("files", ProductFile.class, QProductFile.class, PathInits.DIRECT2);

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final BooleanPath isCoupon = createBoolean("isCoupon");

    public final BooleanPath isSaled = createBoolean("isSaled");

    public final SetPath<Option, QOption> options = this.<Option, QOption>createSet("options", Option.class, QOption.class, PathInits.DIRECT2);

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath productCode = createString("productCode");

    public final StringPath ProductDesc = createString("ProductDesc");

    public final QProductDetails productDetails;

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productName = createString("productName");

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final StringPath sellerId = createString("sellerId");

    public final NumberPath<Integer> shippingFee = createNumber("shippingFee", Integer.class);

    public final NumberPath<Integer> shippingTerms = createNumber("shippingTerms", Integer.class);

    public final NumberPath<Integer> sold = createNumber("sold", Integer.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.productDetails = inits.isInitialized("productDetails") ? new QProductDetails(forProperty("productDetails")) : null;
    }

}

