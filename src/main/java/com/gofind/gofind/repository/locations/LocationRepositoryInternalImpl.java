package com.gofind.gofind.repository.locations;

import com.gofind.gofind.domain.locations.Location;
import com.gofind.gofind.domain.locations.Maison;
import com.gofind.gofind.domain.locations.Piece;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.rowmapper.LocationRowMapper;
import com.gofind.gofind.repository.rowmapper.MaisonRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.HashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Location entity.
 */
@SuppressWarnings("unused")
class LocationRepositoryInternalImpl extends SimpleR2dbcRepository<Location, Long> implements LocationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MaisonRowMapper maisonMapper;
    private final LocationRowMapper locationMapper;

    private static final Table entityTable = Table.aliased("location", EntityManager.ENTITY_ALIAS);
    private static final Table maisonTable = Table.aliased("maison", "maison");

    private final Logger log = LoggerFactory.getLogger(MaisonRepositoryInternalImpl.class);
    private final PieceRepository pieceRepository;

    public LocationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MaisonRowMapper maisonMapper,
        LocationRowMapper locationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        PieceRepository pieceRepository
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Location.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.maisonMapper = maisonMapper;
        this.locationMapper = locationMapper;
        this.pieceRepository = pieceRepository;
    }

    // @Override
    // public Flux<Location> findAllBy(Pageable pageable) {
    //     return createQuery(pageable, null).all();
    // }

    @Override
    public Flux<Location> findAllBy(Pageable pageable) {
        // Create a Flux of Location
        Flux<Location> locationFlux = createQuery(pageable, null).all();

        // For each Maison, find and set its pieces
        return locationFlux.flatMap(location ->
            pieceRepository
                .findByLocation(location.getId())
                .collectList()
                .map(pieces -> {
                    location.setPieces(new HashSet<>(pieces));
                    return location;
                }));
    }

    RowsFetchSpec<Location> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LocationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MaisonSqlHelper.getColumns(maisonTable, "maison"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(maisonTable)
            .on(Column.create("maison_id", entityTable))
            .equals(Column.create("id", maisonTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Location.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Location> findAll() {
        return findAllBy(null);
    }

    // @Override
    // public Mono<Location> findById(Long id) {
    //     Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
    //     return createQuery(null, whereClause).one();
    // }

    @Override
    public Mono<Location> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        Mono<Location> locationMono = createQuery(null, whereClause).one();

        log.debug("!!!!!!!!!!! Location id: {}", id);
        Flux<Piece> piecesFlux = pieceRepository.findByLocation(id);

        return locationMono.zipWith(piecesFlux.collectList(), (location, pieces) -> {
            location.setPieces(new HashSet<>(pieces));

            log.debug("!!!!!!!!!!! Pieces found: {}", pieces);
            log.debug("!!!!!!!!!!! Maisons found: {}", location);
            return location;
        });
    }

    private Location process(Row row, RowMetadata metadata) {
        Location entity = locationMapper.apply(row, "e");
        entity.setMaison(maisonMapper.apply(row, "maison"));
        return entity;
    }

    @Override
    public <S extends Location> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
