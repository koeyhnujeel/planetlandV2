package com.planetlandV2.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTradeHistory is a Querydsl query type for TradeHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTradeHistory extends EntityPathBase<TradeHistory> {

    private static final long serialVersionUID = 119833432L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTradeHistory tradeHistory = new QTradeHistory("tradeHistory");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath planetName = createString("planetName");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final EnumPath<com.planetlandV2.Enum.TradeType> tradeType = createEnum("tradeType", com.planetlandV2.Enum.TradeType.class);

    public final QUser user;

    public QTradeHistory(String variable) {
        this(TradeHistory.class, forVariable(variable), INITS);
    }

    public QTradeHistory(Path<? extends TradeHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTradeHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTradeHistory(PathMetadata metadata, PathInits inits) {
        this(TradeHistory.class, metadata, inits);
    }

    public QTradeHistory(Class<? extends TradeHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

