package com.gofind.gofind.service;

import com.gofind.gofind.domain.Location;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.gofind.gofind.domain.Location}.
 */
public interface LocationService {
    /**
     * Save a location.
     *
     * @param location the entity to save.
     * @return the persisted entity.
     */
    Mono<Location> save(Location location);

    /**
     * Updates a location.
     *
     * @param location the entity to update.
     * @return the persisted entity.
     */
    Mono<Location> update(Location location);

    /**
     * Partially updates a location.
     *
     * @param location the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Location> partialUpdate(Location location);

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Location> findAll(Pageable pageable);

    /**
     * Returns the number of locations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" location.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Location> findOne(Long id);

    /**
     * Delete the "id" location.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
