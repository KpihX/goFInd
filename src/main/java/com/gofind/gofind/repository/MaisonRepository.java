package com.gofind.gofind.repository;

import com.gofind.gofind.domain.Maison;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Maison entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaisonRepository extends ReactiveCrudRepository<Maison, Long>, MaisonRepositoryInternal {
    Flux<Maison> findAllBy(Pageable pageable);

    @Query("SELECT * FROM maison entity WHERE entity.proprietaire_id = :id")
    Flux<Maison> findByProprietaire(Long id);

    @Query("SELECT * FROM maison entity WHERE entity.proprietaire_id IS NULL")
    Flux<Maison> findAllWhereProprietaireIsNull();

    @Override
    <S extends Maison> Mono<S> save(S entity);

    @Override
    Flux<Maison> findAll();

    @Override
    Mono<Maison> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MaisonRepositoryInternal {
    <S extends Maison> Mono<S> save(S entity);

    Flux<Maison> findAllBy(Pageable pageable);

    Flux<Maison> findAll();

    Mono<Maison> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Maison> findAllBy(Pageable pageable, Criteria criteria);
}
