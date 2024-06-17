package com.gofind.gofind.web.rest;

import com.gofind.gofind.domain.locations.Piece;
import com.gofind.gofind.repository.locations.PieceRepository;
import com.gofind.gofind.service.locations.PieceService;
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
 * REST controller for managing {@link com.gofind.gofind.domain.locations.Piece}.
 */
@RestController
@RequestMapping("/api/pieces")
public class PieceResource {

    private final Logger log = LoggerFactory.getLogger(PieceResource.class);

    private static final String ENTITY_NAME = "piece";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PieceService pieceService;

    private final PieceRepository pieceRepository;

    public PieceResource(PieceService pieceService, PieceRepository pieceRepository) {
        this.pieceService = pieceService;
        this.pieceRepository = pieceRepository;
    }

    /**
     * {@code POST  /pieces} : Create a new piece.
     *
     * @param piece the piece to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new piece, or with status {@code 400 (Bad Request)} if the piece has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Piece>> createPiece(@Valid @RequestBody Piece piece) throws URISyntaxException {
        log.debug("REST request to save Piece : {}", piece);
        if (piece.getId() != null) {
            throw new BadRequestAlertException("A new piece cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pieceService
            .save(piece)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/pieces/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /pieces/:id} : Updates an existing piece.
     *
     * @param id the id of the piece to save.
     * @param piece the piece to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piece,
     * or with status {@code 400 (Bad Request)} if the piece is not valid,
     * or with status {@code 500 (Internal Server Error)} if the piece couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Piece>> updatePiece(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Piece piece
    ) throws URISyntaxException {
        log.debug("REST request to update Piece : {}, {}", id, piece);
        if (piece.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, piece.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pieceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pieceService
                    .update(piece)
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
     * {@code PATCH  /pieces/:id} : Partial updates given fields of an existing piece, field will ignore if it is null
     *
     * @param id the id of the piece to save.
     * @param piece the piece to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piece,
     * or with status {@code 400 (Bad Request)} if the piece is not valid,
     * or with status {@code 404 (Not Found)} if the piece is not found,
     * or with status {@code 500 (Internal Server Error)} if the piece couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Piece>> partialUpdatePiece(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Piece piece
    ) throws URISyntaxException {
        log.debug("REST request to partial update Piece partially : {}, {}", id, piece);
        if (piece.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, piece.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pieceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Piece> result = pieceService.partialUpdate(piece);

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
     * {@code GET  /pieces} : get all the pieces.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pieces in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Piece>>> getAllPieces(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Pieces");
        return pieceService
            .countAll()
            .zipWith(pieceService.findAll(pageable).collectList())
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
     * {@code GET  /pieces/:id} : get the "id" piece.
     *
     * @param id the id of the piece to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the piece, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Piece>> getPiece(@PathVariable("id") Long id) {
        log.debug("REST request to get Piece : {}", id);
        Mono<Piece> piece = pieceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(piece);
    }

    /**
     * {@code DELETE  /pieces/:id} : delete the "id" piece.
     *
     * @param id the id of the piece to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePiece(@PathVariable("id") Long id) {
        log.debug("REST request to delete Piece : {}", id);
        return pieceService
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
