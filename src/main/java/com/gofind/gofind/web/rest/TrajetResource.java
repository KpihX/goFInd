package com.gofind.gofind.web.rest;

import com.gofind.gofind.domain.itinaries.Trajet;
import com.gofind.gofind.domain.users.Utilisateur;
import com.gofind.gofind.repository.itinaries.TrajetRepository;
import com.gofind.gofind.service.itinaries.TrajetService;
import com.gofind.gofind.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.gofind.gofind.domain.itinaries.Trajet}.
 */
@RestController
@RequestMapping("/api/trajets")
public class TrajetResource {

    private final Logger log = LoggerFactory.getLogger(TrajetResource.class);

    private static final String ENTITY_NAME = "trajet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrajetService trajetService;

    private final TrajetRepository trajetRepository;

    public TrajetResource(TrajetService trajetService, TrajetRepository trajetRepository) {
        this.trajetService = trajetService;
        this.trajetRepository = trajetRepository;
    }

    /**
     * {@code POST  /trajets} : Create a new trajet.
     *
     * @param trajet the trajet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trajet, or with status {@code 400 (Bad Request)} if the trajet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Trajet>> createTrajet(@Valid @RequestBody Trajet trajet) throws URISyntaxException {
        log.debug("REST request to save Trajet : {}", trajet);
        if (trajet.getId() != null) {
            throw new BadRequestAlertException("A new trajet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return trajetService
            .save(trajet)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/trajets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /trajets/:id} : Updates an existing trajet.
     *
     * @param id the id of the trajet to save.
     * @param trajet the trajet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trajet,
     * or with status {@code 400 (Bad Request)} if the trajet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trajet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Trajet>> updateTrajet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Trajet trajet
        // @RequestParam(required = false) Set<Utilisateur> engages
    ) throws URISyntaxException {
        log.debug("!!!!!!!!!!!!!!! REST request to update Trajet : {}, {}", id, trajet);
        if (trajet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trajet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        log.debug("!!!!!!!!!!!!!!! Engages : {}", trajet.getEngages());
        // trajet.setEngages(engages);

        return trajetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return trajetService
                    .update(trajet)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /trajets/:id} : Partial updates given fields of an existing trajet, field will ignore if it is null
     *
     * @param id the id of the trajet to save.
     * @param trajet the trajet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trajet,
     * or with status {@code 400 (Bad Request)} if the trajet is not valid,
     * or with status {@code 404 (Not Found)} if the trajet is not found,
     * or with status {@code 500 (Internal Server Error)} if the trajet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Trajet>> partialUpdateTrajet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Trajet trajet
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trajet partially : {}, {}", id, trajet);
        if (trajet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trajet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return trajetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Trajet> result = trajetService.partialUpdate(trajet);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /trajets} : get all the trajets.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trajets in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Trajet>>> getAllTrajets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Trajets");
        return trajetService
            .countAll()
            .zipWith(trajetService.findAll(pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /trajets/:id} : get the "id" trajet.
     *
     * @param id the id of the trajet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trajet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Trajet>> getTrajet(@PathVariable("id") Long id) {
        log.debug("REST request to get Trajet : {}", id);
        Mono<Trajet> trajet = trajetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trajet);
    }

    /**
     * {@code DELETE  /trajets/:id} : delete the "id" trajet.
     *
     * @param id the id of the trajet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTrajet(@PathVariable("id") Long id) {
        log.debug("REST request to delete Trajet : {}", id);
        return trajetService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
