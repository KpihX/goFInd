package com.gofind.gofind.web.rest;

import static com.gofind.gofind.domain.PieceAsserts.*;
import static com.gofind.gofind.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofind.gofind.IntegrationTest;
import com.gofind.gofind.domain.enumeration.EtatPiece;
import com.gofind.gofind.domain.locations.Piece;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.locations.PieceRepository;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PieceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PieceResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final EtatPiece DEFAULT_ETAT = EtatPiece.LOUE;
    private static final EtatPiece UPDATED_ETAT = EtatPiece.NONLOUE;

    private static final String ENTITY_API_URL = "/api/pieces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PieceRepository pieceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Piece piece;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piece createEntity(EntityManager em) {
        Piece piece = new Piece().libelle(DEFAULT_LIBELLE).image(DEFAULT_IMAGE).etat(DEFAULT_ETAT);
        return piece;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piece createUpdatedEntity(EntityManager em) {
        Piece piece = new Piece().libelle(UPDATED_LIBELLE).image(UPDATED_IMAGE).etat(UPDATED_ETAT);
        return piece;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Piece.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        piece = createEntity(em);
    }

    @Test
    void createPiece() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Piece
        var returnedPiece = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Piece.class)
            .returnResult()
            .getResponseBody();

        // Validate the Piece in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPieceUpdatableFieldsEquals(returnedPiece, getPersistedPiece(returnedPiece));
    }

    @Test
    void createPieceWithExistingId() throws Exception {
        // Create the Piece with an existing ID
        piece.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Piece in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        piece.setLibelle(null);

        // Create the Piece, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPieces() {
        // Initialize the database
        pieceRepository.save(piece).block();

        // Get all the pieceList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(piece.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].image")
            .value(hasItem(DEFAULT_IMAGE))
            .jsonPath("$.[*].etat")
            .value(hasItem(DEFAULT_ETAT.toString()));
    }

    @Test
    void getPiece() {
        // Initialize the database
        pieceRepository.save(piece).block();

        // Get the piece
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, piece.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(piece.getId().intValue()))
            .jsonPath("$.libelle")
            .value(is(DEFAULT_LIBELLE))
            .jsonPath("$.image")
            .value(is(DEFAULT_IMAGE))
            .jsonPath("$.etat")
            .value(is(DEFAULT_ETAT.toString()));
    }

    @Test
    void getNonExistingPiece() {
        // Get the piece
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPiece() throws Exception {
        // Initialize the database
        pieceRepository.save(piece).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the piece
        Piece updatedPiece = pieceRepository.findById(piece.getId()).block();
        updatedPiece.libelle(UPDATED_LIBELLE).image(UPDATED_IMAGE).etat(UPDATED_ETAT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPiece.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedPiece))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Piece in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPieceToMatchAllProperties(updatedPiece);
    }

    @Test
    void putNonExistingPiece() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piece.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, piece.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Piece in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPiece() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piece.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Piece in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPiece() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piece.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Piece in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePieceWithPatch() throws Exception {
        // Initialize the database
        pieceRepository.save(piece).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the piece using partial update
        Piece partialUpdatedPiece = new Piece();
        partialUpdatedPiece.setId(piece.getId());

        partialUpdatedPiece.image(UPDATED_IMAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPiece.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPiece))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Piece in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPieceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPiece, piece), getPersistedPiece(piece));
    }

    @Test
    void fullUpdatePieceWithPatch() throws Exception {
        // Initialize the database
        pieceRepository.save(piece).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the piece using partial update
        Piece partialUpdatedPiece = new Piece();
        partialUpdatedPiece.setId(piece.getId());

        partialUpdatedPiece.libelle(UPDATED_LIBELLE).image(UPDATED_IMAGE).etat(UPDATED_ETAT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPiece.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPiece))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Piece in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPieceUpdatableFieldsEquals(partialUpdatedPiece, getPersistedPiece(partialUpdatedPiece));
    }

    @Test
    void patchNonExistingPiece() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piece.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, piece.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Piece in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPiece() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piece.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Piece in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPiece() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piece.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(piece))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Piece in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePiece() {
        // Initialize the database
        pieceRepository.save(piece).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the piece
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, piece.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pieceRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Piece getPersistedPiece(Piece piece) {
        return pieceRepository.findById(piece.getId()).block();
    }

    protected void assertPersistedPieceToMatchAllProperties(Piece expectedPiece) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPieceAllPropertiesEquals(expectedPiece, getPersistedPiece(expectedPiece));
        assertPieceUpdatableFieldsEquals(expectedPiece, getPersistedPiece(expectedPiece));
    }

    protected void assertPersistedPieceToMatchUpdatableProperties(Piece expectedPiece) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPieceAllUpdatablePropertiesEquals(expectedPiece, getPersistedPiece(expectedPiece));
        assertPieceUpdatableFieldsEquals(expectedPiece, getPersistedPiece(expectedPiece));
    }
}
