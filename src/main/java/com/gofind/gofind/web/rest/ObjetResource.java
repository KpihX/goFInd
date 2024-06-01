package com.gofind.gofind.web.rest;

import com.gofind.gofind.domain.Objet;
import com.gofind.gofind.repository.ObjetRepository;
import com.gofind.gofind.service.ObjetService;
import com.gofind.gofind.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
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
 * REST controller for managing {@link com.gofind.gofind.domain.Objet}.
 */
@RestController
@RequestMapping("/api/objets")
public class ObjetResource {

    private final Logger log = LoggerFactory.getLogger(ObjetResource.class);

    private static final String ENTITY_NAME = "objet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjetService objetService;

    private final ObjetRepository objetRepository;

    public ObjetResource(ObjetService objetService, ObjetRepository objetRepository) {
        this.objetService = objetService;
        this.objetRepository = objetRepository;
    }

    /**
     * {@code POST  /objets} : Create a new objet.
     *
     * @param objet the objet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objet, or with status {@code 400 (Bad Request)} if the objet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Objet>> createObjet(@Valid @RequestBody Objet objet) throws URISyntaxException {
        log.debug("REST request to save Objet : {}", objet);
        if (objet.getId() != null) {
            throw new BadRequestAlertException("A new objet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return objetService
            .save(objet)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/objets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /objets/:id} : Updates an existing objet.
     *
     * @param id the id of the objet to save.
     * @param objet the objet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objet,
     * or with status {@code 400 (Bad Request)} if the objet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Objet>> updateObjet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Objet objet
    ) throws URISyntaxException {
        log.debug("REST request to update Objet : {}, {}", id, objet);
        if (objet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return objetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return objetService
                    .update(objet)
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
     * {@code PATCH  /objets/:id} : Partial updates given fields of an existing objet, field will ignore if it is null
     *
     * @param id the id of the objet to save.
     * @param objet the objet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objet,
     * or with status {@code 400 (Bad Request)} if the objet is not valid,
     * or with status {@code 404 (Not Found)} if the objet is not found,
     * or with status {@code 500 (Internal Server Error)} if the objet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Objet>> partialUpdateObjet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Objet objet
    ) throws URISyntaxException {
        log.debug("REST request to partial update Objet partially : {}, {}", id, objet);
        if (objet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return objetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Objet> result = objetService.partialUpdate(objet);

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
     * {@code GET  /objets} : get all the objets.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objets in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Objet>>> getAllObjets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = true) String search,
        @RequestParam(required = true) String searchType,
        ServerHttpRequest request
    ) {
        log.debug("*** REST request to get a page of Objets");
        // log.debug("*** Search: " + search);
        // log.debug("*** SearchType: " + searchType);
        return objetService
            .countAll()
            .zipWith(objetService.findAll(pageable, search, searchType).collectList())
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
     * {@code GET  /objets/:id} : get the "id" objet.
     *
     * @param id the id of the objet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Objet>> getObjet(@PathVariable("id") Long id) {
        log.debug("REST request to get Objet : {}", id);
        Mono<Objet> objet = objetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(objet);
    }

    /**
     * {@code DELETE  /objets/:id} : delete the "id" objet.
     *
     * @param id the id of the objet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteObjet(@PathVariable("id") Long id) {
        log.debug("REST request to delete Objet : {}", id);
        return objetService
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
