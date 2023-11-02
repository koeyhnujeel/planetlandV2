package com.planetlandV2.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanet is a Querydsl query type for Planet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanet extends EntityPathBase<Planet> {

    private static final long serialVersionUID = 1027356768L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanet planet = new QPlanet("planet");

    public final StringPath imgName = createString("imgName");

    public final StringPath imgPath = createString("imgPath");

    public final StringPath owner = createString("owner");

    public final NumberPath<Long> planetId = createNumber("planetId", Long.class);

    public final StringPath planetName = createString("planetName");

    public final EnumPath<com.planetlandV2.Enum.PlanetStatus> planetStatus = createEnum("planetStatus", com.planetlandV2.Enum.PlanetStatus.class);

    public final NumberPath<Integer> population = createNumber("population", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> satellite = createNumber("satellite", Integer.class);

    public final QUser user;

    public QPlanet(String variable) {
        this(Planet.class, forVariable(variable), INITS);
    }

    public QPlanet(Path<? extends Planet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanet(PathMetadata metadata, PathInits inits) {
        this(Planet.class, metadata, inits);
    }

    public QPlanet(Class<? extends Planet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

