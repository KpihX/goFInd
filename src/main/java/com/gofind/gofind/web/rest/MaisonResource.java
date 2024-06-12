package com.gofind.gofind.web.rest;

import com.gofind.gofind.domain.locations.Maison;
import com.gofind.gofind.repository.locations.MaisonRepository;
import com.gofind.gofind.service.locations.MaisonService;
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
 * REST controller for managing {@link com.gofind.gofind.domain.locations.Maison}.
 */
@RestController
@RequestMapping("/api/maisons")
public class MaisonResource {

    private final Logger log = LoggerFactory.getLogger(MaisonResource.class);

    private static final String ENTITY_NAME = "maison";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaisonService maisonService;

    private final MaisonRepository maisonRepository;

    public MaisonResource(MaisonService maisonService, MaisonRepository maisonRepository) {
        this.maisonService = maisonService;
        this.maisonRepository = maisonRepository;
    }

    /**
     * {@code POST  /maisons} : Create a new maison.
     *
     * @param maison the maison to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maison, or with status {@code 400 (Bad Request)} if the maison has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Maison>> createMaison(@Valid @RequestBody Maison maison) throws URISyntaxException {
        log.debug("REST request to save Maison : {}", maison);
        if (maison.getId() != null) {
            throw new BadRequestAlertException("A new maison cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return maisonService
            .save(maison)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/maisons/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /maisons/:id} : Updates an existing maison.
     *
     * @param id the id of the maison to save.
     * @param maison the maison to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maison,
     * or with status {@code 400 (Bad Request)} if the maison is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maison couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Maison>> updateMaison(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Maison maison
    ) throws URISyntaxException {
        log.debug("REST request to update Maison : {}, {}", id, maison);
        if (maison.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maison.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return maisonRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return maisonService
                    .update(maison)
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
     * {@code PATCH  /maisons/:id} : Partial updates given fields of an existing maison, field will ignore if it is null
     *
     * @param id the id of the maison to save.
     * @param maison the maison to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maison,
     * or with status {@code 400 (Bad Request)} if the maison is not valid,
     * or with status {@code 404 (Not Found)} if the maison is not found,
     * or with status {@code 500 (Internal Server Error)} if the maison couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Maison>> partialUpdateMaison(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Maison maison
    ) throws URISyntaxException {
        log.debug("REST request to partial update Maison partially : {}, {}", id, maison);
        if (maison.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maison.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return maisonRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Maison> result = maisonService.partialUpdate(maison);

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
     * {@code GET  /maisons} : get all the maisons.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maisons in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Maison>>> getAllMaisons(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Maisons");
        return maisonService
            .countAll()
            .zipWith(maisonService.findAll(pageable).collectList())
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
     * {@code GET  /maisons/:id} : get the "id" maison.
     *
     * @param id the id of the maison to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maison, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Maison>> getMaison(@PathVariable("id") Long id) {
        log.debug("REST request to get Maison : {}", id);
        Mono<Maison> maison = maisonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maison);
    }

    /**
     * {@code DELETE  /maisons/:id} : delete the "id" maison.
     *
     * @param id the id of the maison to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMaison(@PathVariable("id") Long id) {
        log.debug("REST request to delete Maison : {}", id);
        return maisonService
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
