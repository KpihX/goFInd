package com.gofind.gofind.service.itinaries;

import com.gofind.gofind.domain.itinaries.Trajet;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.gofind.gofind.domain.itinaries.Trajet}.
 */
public interface TrajetService {
    /**
     * Save a trajet.
     *
     * @param trajet the entity to save.
     * @return the persisted entity.
     */
    Mono<Trajet> save(Trajet trajet);

    /**
     * Updates a trajet.
     *
     * @param trajet the entity to update.
     * @return the persisted entity.
     */
    Mono<Trajet> update(Trajet trajet);

    /**
     * Partially updates a trajet.
     *
     * @param trajet the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Trajet> partialUpdate(Trajet trajet);

    /**
     * Get all the trajets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Trajet> findAll(Pageable pageable);

    /**
     * Returns the number of trajets available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" trajet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Trajet> findOne(Long id);

    /**
     * Delete the "id" trajet.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
