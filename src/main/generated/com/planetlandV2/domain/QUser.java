package com.planetlandV2.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1762116755L;

    public static final QUser user = new QUser("user");

    public final NumberPath<Integer> balance = createNumber("balance", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<Planet, QPlanet> planets = this.<Planet, QPlanet>createList("planets", Planet.class, QPlanet.class, PathInits.DIRECT2);

    public final ListPath<Session, QSession> sessions = this.<Session, QSession>createList("sessions", Session.class, QSession.class, PathInits.DIRECT2);

    public final ListPath<TradeHistory, QTradeHistory> tradeHistoryList = this.<TradeHistory, QTradeHistory>createList("tradeHistoryList", TradeHistory.class, QTradeHistory.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

