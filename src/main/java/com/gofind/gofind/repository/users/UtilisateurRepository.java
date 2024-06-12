package com.gofind.gofind.repository.users;

import com.gofind.gofind.domain.users.Utilisateur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Utilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurRepository extends ReactiveCrudRepository<Utilisateur, Long>, UtilisateurRepositoryInternal {
    @Override
    Mono<Utilisateur> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Utilisateur> findAllWithEagerRelationships();

    @Override
    Flux<Utilisateur> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM utilisateur entity WHERE entity.login_id = :id")
    Flux<Utilisateur> findByLogin(Long id);

    @Query("SELECT * FROM utilisateur entity WHERE entity.login_id IS NULL")
    Flux<Utilisateur> findAllWhereLoginIsNull();

    @Query(
        "SELECT entity.* FROM utilisateur entity JOIN rel_utilisateur__trajets joinTable ON entity.id = joinTable.trajets_id WHERE joinTable.trajets_id = :id"
    )
    Flux<Utilisateur> findByTrajets(Long id);

    @Override
    <S extends Utilisateur> Mono<S> save(S entity);

    @Override
    Flux<Utilisateur> findAll();

    @Override
    Mono<Utilisateur> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UtilisateurRepositoryInternal {
    <S extends Utilisateur> Mono<S> save(S entity);

    Flux<Utilisateur> findAllBy(Pageable pageable);

    Flux<Utilisateur> findAll();

    Mono<Utilisateur> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Utilisateur> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Utilisateur> findOneWithEagerRelationships(Long id);

    Flux<Utilisateur> findAllWithEagerRelationships();

    Flux<Utilisateur> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
