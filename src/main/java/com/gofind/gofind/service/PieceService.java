package com.gofind.gofind.service;

import com.gofind.gofind.domain.Piece;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.gofind.gofind.domain.Piece}.
 */
public interface PieceService {
    /**
     * Save a piece.
     *
     * @param piece the entity to save.
     * @return the persisted entity.
     */
    Mono<Piece> save(Piece piece);

    /**
     * Updates a piece.
     *
     * @param piece the entity to update.
     * @return the persisted entity.
     */
    Mono<Piece> update(Piece piece);

    /**
     * Partially updates a piece.
     *
     * @param piece the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Piece> partialUpdate(Piece piece);

    /**
     * Get all the pieces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Piece> findAll(Pageable pageable);

    /**
     * Returns the number of pieces available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" piece.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Piece> findOne(Long id);

    /**
     * Delete the "id" piece.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
