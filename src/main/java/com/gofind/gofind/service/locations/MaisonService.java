package com.gofind.gofind.service.locations;

import com.gofind.gofind.domain.locations.Maison;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.gofind.gofind.domain.locations.Maison}.
 */
public interface MaisonService {
    /**
     * Save a maison.
     *
     * @param maison the entity to save.
     * @return the persisted entity.
     */
    Mono<Maison> save(Maison maison);

    /**
     * Updates a maison.
     *
     * @param maison the entity to update.
     * @return the persisted entity.
     */
    Mono<Maison> update(Maison maison);

    /**
     * Partially updates a maison.
     *
     * @param maison the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Maison> partialUpdate(Maison maison);

    /**
     * Get all the maisons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Maison> findAll(Pageable pageable);

    /**
     * Returns the number of maisons available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" maison.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Maison> findOne(Long id);

    /**
     * Delete the "id" maison.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
