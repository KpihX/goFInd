package com.gofind.gofind.repository;

import com.gofind.gofind.domain.Piece;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Piece entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PieceRepository extends ReactiveCrudRepository<Piece, Long>, PieceRepositoryInternal {
    Flux<Piece> findAllBy(Pageable pageable);

    @Query("SELECT * FROM piece entity WHERE entity.maison_id = :id")
    Flux<Piece> findByMaison(Long id);

    @Query("SELECT * FROM piece entity WHERE entity.maison_id IS NULL")
    Flux<Piece> findAllWhereMaisonIsNull();

    @Query("SELECT * FROM piece entity WHERE entity.location_id = :id")
    Flux<Piece> findByLocation(Long id);

    @Query("SELECT * FROM piece entity WHERE entity.location_id IS NULL")
    Flux<Piece> findAllWhereLocationIsNull();

    @Override
    <S extends Piece> Mono<S> save(S entity);

    @Override
    Flux<Piece> findAll();

    @Override
    Mono<Piece> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PieceRepositoryInternal {
    <S extends Piece> Mono<S> save(S entity);

    Flux<Piece> findAllBy(Pageable pageable);

    Flux<Piece> findAll();

    Mono<Piece> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Piece> findAllBy(Pageable pageable, Criteria criteria);
}
