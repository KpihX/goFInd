package com.gofind.gofind.repository;

import com.gofind.gofind.domain.Objet;
import com.gofind.gofind.repository.rowmapper.ObjetRowMapper;
import com.gofind.gofind.repository.rowmapper.UtilisateurRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Objet entity.
 */
@SuppressWarnings("unused")
class ObjetRepositoryInternalImpl extends SimpleR2dbcRepository<Objet, Long> implements ObjetRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UtilisateurRowMapper utilisateurMapper;
    private final ObjetRowMapper objetMapper;

    private static final Table entityTable = Table.aliased("objet", EntityManager.ENTITY_ALIAS);
    private static final Table proprietaireTable = Table.aliased("utilisateur", "proprietaire");
    private static final Table signalantTable = Table.aliased("utilisateur", "signalant");

    private final Logger log = LoggerFactory.getLogger(ObjetRepositoryInternalImpl.class);

    public ObjetRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UtilisateurRowMapper utilisateurMapper,
        ObjetRowMapper objetMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Objet.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.utilisateurMapper = utilisateurMapper;
        this.objetMapper = objetMapper;
    }

    @Override
    public Flux<Objet> findAllBy(Pageable pageable, String search, String searchType) {
        Expression searchExpression = SQL.literalOf("%" + search + "%");
        log.debug("*** Search: " + search);
        log.debug("*** SearchType: " + searchType);
        Condition whereClause = Conditions.like(entityTable.column(searchType), searchExpression);
        return createQuery(pageable, whereClause).all();
    }

    RowsFetchSpec<Objet> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ObjetSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UtilisateurSqlHelper.getColumns(proprietaireTable, "proprietaire"));
        columns.addAll(UtilisateurSqlHelper.getColumns(signalantTable, "signalant"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(proprietaireTable)
            .on(Column.create("proprietaire_id", entityTable))
            .equals(Column.create("id", proprietaireTable))
            .leftOuterJoin(signalantTable)
            .on(Column.create("signalant_id", entityTable))
            .equals(Column.create("id", signalantTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Objet.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Objet> findAll(String search, String searchType) {
        return findAllBy(null, search, searchType);
    }

    @Override
    public Mono<Objet> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Objet process(Row row, RowMetadata metadata) {
        Objet entity = objetMapper.apply(row, "e");
        entity.setProprietaire(utilisateurMapper.apply(row, "proprietaire"));
        entity.setSignalant(utilisateurMapper.apply(row, "signalant"));
        return entity;
    }

    @Override
    public <S extends Objet> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
