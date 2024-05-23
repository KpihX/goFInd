package com.gofind.gofind.service;

import com.gofind.gofind.domain.Utilisateur;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.gofind.gofind.domain.Utilisateur}.
 */
public interface UtilisateurService {
    /**
     * Save a utilisateur.
     *
     * @param utilisateur the entity to save.
     * @return the persisted entity.
     */
    Mono<Utilisateur> save(Utilisateur utilisateur);

    /**
     * Updates a utilisateur.
     *
     * @param utilisateur the entity to update.
     * @return the persisted entity.
     */
    Mono<Utilisateur> update(Utilisateur utilisateur);

    /**
     * Partially updates a utilisateur.
     *
     * @param utilisateur the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Utilisateur> partialUpdate(Utilisateur utilisateur);

    /**
     * Get all the utilisateurs.
     *
     * @return the list of entities.
     */
    Flux<Utilisateur> findAll();

    /**
     * Get all the utilisateurs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Utilisateur> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of utilisateurs available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" utilisateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Utilisateur> findOne(Long id);

    /**
     * Delete the "id" utilisateur.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
