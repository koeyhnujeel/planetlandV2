package com.planetlandV2.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPlanet is a Querydsl query type for Planet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanet extends EntityPathBase<Planet> {

    private static final long serialVersionUID = 1027356768L;

    public static final QPlanet planet = new QPlanet("planet");

    public final StringPath imgName = createString("imgName");

    public final StringPath imgPath = createString("imgPath");

    public final NumberPath<Long> planetId = createNumber("planetId", Long.class);

    public final StringPath planetName = createString("planetName");

    public final StringPath planetStatus = createString("planetStatus");

    public final NumberPath<Integer> population = createNumber("population", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> satellite = createNumber("satellite", Integer.class);

    public QPlanet(String variable) {
        super(Planet.class, forVariable(variable));
    }

    public QPlanet(Path<? extends Planet> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlanet(PathMetadata metadata) {
        super(Planet.class, metadata);
    }

}

