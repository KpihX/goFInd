package com.gofind.gofind.repository.locations;

import com.gofind.gofind.domain.itinaries.Trajet;
import com.gofind.gofind.domain.locations.Maison;
import com.gofind.gofind.domain.locations.Piece;
import com.gofind.gofind.domain.users.Utilisateur;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.rowmapper.MaisonRowMapper;
import com.gofind.gofind.repository.rowmapper.UtilisateurRowMapper;
import com.gofind.gofind.repository.users.UtilisateurSqlHelper;
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

    private final Logger log = LoggerFactory.getLogger(MaisonRepositoryInternalImpl.class);
    private final PieceRepository pieceRepository;

    public MaisonRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UtilisateurRowMapper utilisateurMapper,
        MaisonRowMapper maisonMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        PieceRepository pieceRepository
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
        this.pieceRepository = pieceRepository;
    }

    @Override
    public Flux<Maison> findAllBy(Pageable pageable, String search, String searchType) {
        Expression searchExpression = SQL.literalOf("%" + search + "%");
        log.debug("*** Search: " + search);
        log.debug("*** SearchType: " + searchType);
        Condition whereClause = Conditions.like(entityTable.column(searchType), searchExpression);

        // Create a Flux of Maison
        Flux<Maison> maisonFlux = createQuery(pageable, whereClause).all();

        // For each Maison, find and set its pieces
        return maisonFlux.flatMap(maison ->
            pieceRepository
                .findByMaison(maison.getId())
                .collectList()
                .map(pieces -> {
                    maison.setPieces(new HashSet<>(pieces));
                    return maison;
                }));
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
    public Flux<Maison> findAll(String search, String searchType) {
        return findAllBy(null, search, searchType);
    }

    @Override
    public Mono<Maison> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        Mono<Maison> maisonMono = createQuery(null, whereClause).one();

        log.debug("!!!!!!!!!!! Maison id: {}", id);
        Flux<Piece> piecesFlux = pieceRepository.findByMaison(id);

        return maisonMono.zipWith(piecesFlux.collectList(), (maison, pieces) -> {
            maison.setPieces(new HashSet<>(pieces));

            log.debug("!!!!!!!!!!! Pieces found: {}", pieces);
            log.debug("!!!!!!!!!!! Maisons found: {}", maison);
            return maison;
        });
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
