package com.gofind.gofind.repository.itinaries;

import com.gofind.gofind.domain.itinaries.Trajet;
import com.gofind.gofind.domain.users.Utilisateur;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.rowmapper.TrajetRowMapper;
import com.gofind.gofind.repository.rowmapper.UtilisateurRowMapper;
import com.gofind.gofind.repository.users.UtilisateurRepository;
import com.gofind.gofind.repository.users.UtilisateurSqlHelper;
import com.gofind.gofind.web.rest.AccountResource;
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
 * Spring Data R2DBC custom repository implementation for the Trajet entity.
 */
@SuppressWarnings("unused")
class TrajetRepositoryInternalImpl extends SimpleR2dbcRepository<Trajet, Long> implements TrajetRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UtilisateurRowMapper utilisateurMapper;
    private final TrajetRowMapper trajetMapper;

    private static final Table entityTable = Table.aliased("trajet", EntityManager.ENTITY_ALIAS);
    private static final Table proprietaireTable = Table.aliased("utilisateur", "proprietaire");

    private final UtilisateurRepository utilisateurRepository;
    private final Logger log = LoggerFactory.getLogger(TrajetRepositoryInternalImpl.class);

    private static final EntityManager.LinkTable trajetsLink = new EntityManager.LinkTable(
        "rel_utilisateur__trajets",
        "trajets_id",
        "utilisateur_id"
    );

    public TrajetRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UtilisateurRowMapper utilisateurMapper,
        TrajetRowMapper trajetMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        UtilisateurRepository utilisateurRepository
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Trajet.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.utilisateurMapper = utilisateurMapper;
        this.trajetMapper = trajetMapper;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Flux<Trajet> findAllBy(Pageable pageable, String search, String search2) {
        // Create a Flux of Trajet
        Expression searchExpression = SQL.literalOf("%" + search + "%");
        Expression searchExpression2 = SQL.literalOf("%" + search2 + "%");
        log.debug("*** Search: " + search);
        log.debug("*** SearchType: " + search2);
        // Condition whereClause = Conditions.like(entityTable.column("depart"), searchExpression);

        Condition whereClause = Conditions.like(entityTable.column("depart"), searchExpression).and(
            Conditions.like(entityTable.column("arrivee"), searchExpression2)
        );

        Flux<Trajet> trajetFlux = createQuery(pageable, whereClause).all();

        // For each Trajet, find and set its engages
        return trajetFlux.flatMap(trajet ->
            utilisateurRepository
                .findByTrajets(trajet.getId())
                .collectList()
                .map(engages -> {
                    trajet.setEngages(new HashSet<>(engages));
                    return trajet;
                }));
    }

    RowsFetchSpec<Trajet> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TrajetSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UtilisateurSqlHelper.getColumns(proprietaireTable, "proprietaire"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(proprietaireTable)
            .on(Column.create("proprietaire_id", entityTable))
            .equals(Column.create("id", proprietaireTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Trajet.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Trajet> findAll(String search, String search2) {
        return findAllBy(null, search, search2);
    }

    @Override
    public Mono<Trajet> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        // return createQuery(null, whereClause).one();
        Mono<Trajet> trajetMono = createQuery(null, whereClause).one();

        log.debug("!!!!!!!!!!! Trajet id: {}", id);
        Flux<Utilisateur> engagesFlux = utilisateurRepository.findByTrajets(id);

        // Combine the Trajet with its engages and return
        return trajetMono.zipWith(engagesFlux.collectList(), (trajet, engages) -> {
            trajet.setEngages(new HashSet<>(engages));

            log.debug("!!!!!!!!!!! Engages found: {}", engages);
            log.debug("!!!!!!!!!!! Trajets found: {}", trajet);
            return trajet;
        });
    }

    private Trajet process(Row row, RowMetadata metadata) {
        Trajet entity = trajetMapper.apply(row, "e");
        entity.setProprietaire(utilisateurMapper.apply(row, "proprietaire"));
        return entity;
    }

    @Override
    public <S extends Trajet> Mono<S> save(S entity) {
        Mono<S> entity2 = super.save(entity);
        log.debug("!!!!!!!!!!! Trajet2: {}", entity2);
        Mono<S> entity3 = entity2.flatMap((S e) -> updateRelations(e));
        log.debug("!!!!!!!!!!! Trajet3: {}", entity3);
        return entity3;
    }

    protected <S extends Trajet> Mono<S> updateRelations(S entity) {
        log.debug("!!!!!!!!!!! Trajet: {}", entity);
        Mono<Void> result = entityManager
            .updateLinkTable(trajetsLink, entity.getId(), entity.getEngages().stream().map(Utilisateur::getId))
            .then();
        log.debug("!!!!!!!!!!! Result: {}", result);
        return result.thenReturn(entity);
    }
}
