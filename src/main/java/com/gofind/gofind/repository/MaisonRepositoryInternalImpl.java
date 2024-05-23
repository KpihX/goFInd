package com.gofind.gofind.repository;

import com.gofind.gofind.domain.Maison;
import com.gofind.gofind.repository.rowmapper.MaisonRowMapper;
import com.gofind.gofind.repository.rowmapper.UtilisateurRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Maison entity.
 */
@SuppressWarnings("unused")
class MaisonRepositoryInternalImpl extends SimpleR2dbcRepository<Maison, Long> implements MaisonRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UtilisateurRowMapper utilisateurMapper;
    private final MaisonRowMapper maisonMapper;

    private static final Table entityTable = Table.aliased("maison", EntityManager.ENTITY_ALIAS);
    private static final Table proprietaireTable = Table.aliased("utilisateur", "proprietaire");

    public MaisonRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UtilisateurRowMapper utilisateurMapper,
        MaisonRowMapper maisonMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Maison.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.utilisateurMapper = utilisateurMapper;
        this.maisonMapper = maisonMapper;
    }

    @Override
    public Flux<Maison> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Maison> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MaisonSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UtilisateurSqlHelper.getColumns(proprietaireTable, "proprietaire"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(proprietaireTable)
            .on(Column.create("proprietaire_id", entityTable))
            .equals(Column.create("id", proprietaireTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Maison.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Maison> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Maison> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Maison process(Row row, RowMetadata metadata) {
        Maison entity = maisonMapper.apply(row, "e");
        entity.setProprietaire(utilisateurMapper.apply(row, "proprietaire"));
        return entity;
    }

    @Override
    public <S extends Maison> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
