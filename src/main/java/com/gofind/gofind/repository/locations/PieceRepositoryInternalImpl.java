package com.gofind.gofind.repository.locations;

import com.gofind.gofind.domain.locations.Piece;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.rowmapper.LocationRowMapper;
import com.gofind.gofind.repository.rowmapper.MaisonRowMapper;
import com.gofind.gofind.repository.rowmapper.PieceRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
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
 * Spring Data R2DBC custom repository implementation for the Piece entity.
 */
@SuppressWarnings("unused")
class PieceRepositoryInternalImpl extends SimpleR2dbcRepository<Piece, Long> implements PieceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MaisonRowMapper maisonMapper;
    private final LocationRowMapper locationMapper;
    private final PieceRowMapper pieceMapper;

    private static final Table entityTable = Table.aliased("piece", EntityManager.ENTITY_ALIAS);
    private static final Table maisonTable = Table.aliased("maison", "maison");
    private static final Table locationTable = Table.aliased("location", "location");

    public PieceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MaisonRowMapper maisonMapper,
        LocationRowMapper locationMapper,
        PieceRowMapper pieceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Piece.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.maisonMapper = maisonMapper;
        this.locationMapper = locationMapper;
        this.pieceMapper = pieceMapper;
    }

    @Override
    public Flux<Piece> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Piece> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PieceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MaisonSqlHelper.getColumns(maisonTable, "maison"));
        columns.addAll(LocationSqlHelper.getColumns(locationTable, "location"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(maisonTable)
            .on(Column.create("maison_id", entityTable))
            .equals(Column.create("id", maisonTable))
            .leftOuterJoin(locationTable)
            .on(Column.create("location_id", entityTable))
            .equals(Column.create("id", locationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Piece.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Piece> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Piece> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Piece process(Row row, RowMetadata metadata) {
        Piece entity = pieceMapper.apply(row, "e");
        entity.setMaison(maisonMapper.apply(row, "maison"));
        entity.setLocation(locationMapper.apply(row, "location"));
        return entity;
    }

    @Override
    public <S extends Piece> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
