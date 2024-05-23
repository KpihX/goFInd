package com.gofind.gofind.service;

import com.gofind.gofind.domain.Objet;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.gofind.gofind.domain.Objet}.
 */
public interface ObjetService {
    /**
     * Save a objet.
     *
     * @param objet the entity to save.
     * @return the persisted entity.
     */
    Mono<Objet> save(Objet objet);

    /**
     * Updates a objet.
     *
     * @param objet the entity to update.
     * @return the persisted entity.
     */
    Mono<Objet> update(Objet objet);

    /**
     * Partially updates a objet.
     *
     * @param objet the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Objet> partialUpdate(Objet objet);

    /**
     * Get all the objets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Objet> findAll(Pageable pageable);

    /**
     * Returns the number of objets available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" objet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Objet> findOne(Long id);

    /**
     * Delete the "id" objet.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
