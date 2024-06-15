package com.gofind.gofind.repository.objects;

import com.gofind.gofind.domain.objects.Objet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Objet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjetRepository extends ReactiveCrudRepository<Objet, Long>, ObjetRepositoryInternal {
    Flux<Objet> findAllBy(Pageable pageable);

    @Query("SELECT * FROM objet entity WHERE entity.proprietaire_id = :id")
    Flux<Objet> findByProprietaire(Long id);

    @Query("SELECT * FROM objet entity WHERE entity.proprietaire_id IS NULL")
    Flux<Objet> findAllWhereProprietaireIsNull();

    @Query("SELECT * FROM objet entity WHERE entity.signalant_id = :id")
    Flux<Objet> findBySignalant(Long id);

    @Query("SELECT * FROM objet entity WHERE entity.signalant_id IS NULL")
    Flux<Objet> findAllWhereSignalantIsNull();

    @Override
    <S extends Objet> Mono<S> save(S entity);

    @Override
    Flux<Objet> findAll(String search, String searchType);

    @Override
    Mono<Objet> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ObjetRepositoryInternal {
    <S extends Objet> Mono<S> save(S entity);

    Flux<Objet> findAllBy(Pageable pageable, String search, String searchType);

    Flux<Objet> findAll(String search, String searchType);

    Mono<Objet> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Objet> findAllBy(Pageable pageable, Criteria criteria);
}