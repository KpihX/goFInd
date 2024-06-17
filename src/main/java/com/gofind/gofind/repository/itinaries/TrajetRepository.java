package com.gofind.gofind.repository.itinaries;

import com.gofind.gofind.domain.itinaries.Trajet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Trajet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrajetRepository extends ReactiveCrudRepository<Trajet, Long>, TrajetRepositoryInternal {
    Flux<Trajet> findAllBy(Pageable pageable, String search, String search2);

    @Query("SELECT * FROM trajet entity WHERE entity.proprietaire_id = :id")
    Flux<Trajet> findByProprietaire(Long id);

    @Query("SELECT * FROM trajet entity WHERE entity.proprietaire_id IS NULL")
    Flux<Trajet> findAllWhereProprietaireIsNull();

    @Override
    <S extends Trajet> Mono<S> save(S entity);

    @Override
    Flux<Trajet> findAll(String search, String search2);

    @Override
    Mono<Trajet> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TrajetRepositoryInternal {
    <S extends Trajet> Mono<S> save(S entity);

    Flux<Trajet> findAllBy(Pageable pageable, String search, String search2);

    Flux<Trajet> findAll(String search, String search2);

    Mono<Trajet> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Trajet> findAllBy(Pageable pageable, Criteria criteria);
}
