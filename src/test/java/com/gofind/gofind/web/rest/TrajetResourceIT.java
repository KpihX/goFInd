package com.gofind.gofind.web.rest;

import static com.gofind.gofind.domain.TrajetAsserts.*;
import static com.gofind.gofind.web.rest.TestUtil.createUpdateProxyForBean;
import static com.gofind.gofind.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofind.gofind.IntegrationTest;
import com.gofind.gofind.domain.Trajet;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.TrajetRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link TrajetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TrajetResourceIT {

    private static final String DEFAULT_DEPART = "AAAAAAAAAA";
    private static final String UPDATED_DEPART = "BBBBBBBBBB";

    private static final String DEFAULT_ARRIVEE = "AAAAAAAAAA";
    private static final String UPDATED_ARRIVEE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_HEURE_DEPART = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_HEURE_DEPART = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_PLACES = 1;
    private static final Integer UPDATED_PLACES = 2;

    private static final Float DEFAULT_PRIX = 1F;
    private static final Float UPDATED_PRIX = 2F;

    private static final String ENTITY_API_URL = "/api/trajets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Trajet trajet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trajet createEntity(EntityManager em) {
        Trajet trajet = new Trajet()
            .depart(DEFAULT_DEPART)
            .arrivee(DEFAULT_ARRIVEE)
            .dateHeureDepart(DEFAULT_DATE_HEURE_DEPART)
            .places(DEFAULT_PLACES)
            .prix(DEFAULT_PRIX);
        return trajet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trajet createUpdatedEntity(EntityManager em) {
        Trajet trajet = new Trajet()
            .depart(UPDATED_DEPART)
            .arrivee(UPDATED_ARRIVEE)
            .dateHeureDepart(UPDATED_DATE_HEURE_DEPART)
            .places(UPDATED_PLACES)
            .prix(UPDATED_PRIX);
        return trajet;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Trajet.class).block();
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
        trajet = createEntity(em);
    }

    @Test
    void createTrajet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Trajet
        var returnedTrajet = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Trajet.class)
            .returnResult()
            .getResponseBody();

        // Validate the Trajet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTrajetUpdatableFieldsEquals(returnedTrajet, getPersistedTrajet(returnedTrajet));
    }

    @Test
    void createTrajetWithExistingId() throws Exception {
        // Create the Trajet with an existing ID
        trajet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Trajet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkDepartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trajet.setDepart(null);

        // Create the Trajet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkArriveeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trajet.setArrivee(null);

        // Create the Trajet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDateHeureDepartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trajet.setDateHeureDepart(null);

        // Create the Trajet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPlacesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trajet.setPlaces(null);

        // Create the Trajet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPrixIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trajet.setPrix(null);

        // Create the Trajet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllTrajets() {
        // Initialize the database
        trajetRepository.save(trajet).block();

        // Get all the trajetList
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
            .value(hasItem(trajet.getId().intValue()))
            .jsonPath("$.[*].depart")
            .value(hasItem(DEFAULT_DEPART))
            .jsonPath("$.[*].arrivee")
            .value(hasItem(DEFAULT_ARRIVEE))
            .jsonPath("$.[*].dateHeureDepart")
            .value(hasItem(sameInstant(DEFAULT_DATE_HEURE_DEPART)))
            .jsonPath("$.[*].places")
            .value(hasItem(DEFAULT_PLACES))
            .jsonPath("$.[*].prix")
            .value(hasItem(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    void getTrajet() {
        // Initialize the database
        trajetRepository.save(trajet).block();

        // Get the trajet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, trajet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(trajet.getId().intValue()))
            .jsonPath("$.depart")
            .value(is(DEFAULT_DEPART))
            .jsonPath("$.arrivee")
            .value(is(DEFAULT_ARRIVEE))
            .jsonPath("$.dateHeureDepart")
            .value(is(sameInstant(DEFAULT_DATE_HEURE_DEPART)))
            .jsonPath("$.places")
            .value(is(DEFAULT_PLACES))
            .jsonPath("$.prix")
            .value(is(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    void getNonExistingTrajet() {
        // Get the trajet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTrajet() throws Exception {
        // Initialize the database
        trajetRepository.save(trajet).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trajet
        Trajet updatedTrajet = trajetRepository.findById(trajet.getId()).block();
        updatedTrajet
            .depart(UPDATED_DEPART)
            .arrivee(UPDATED_ARRIVEE)
            .dateHeureDepart(UPDATED_DATE_HEURE_DEPART)
            .places(UPDATED_PLACES)
            .prix(UPDATED_PRIX);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTrajet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedTrajet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Trajet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrajetToMatchAllProperties(updatedTrajet);
    }

    @Test
    void putNonExistingTrajet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trajet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, trajet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Trajet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTrajet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trajet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Trajet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTrajet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trajet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Trajet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTrajetWithPatch() throws Exception {
        // Initialize the database
        trajetRepository.save(trajet).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trajet using partial update
        Trajet partialUpdatedTrajet = new Trajet();
        partialUpdatedTrajet.setId(trajet.getId());

        partialUpdatedTrajet.depart(UPDATED_DEPART).places(UPDATED_PLACES).prix(UPDATED_PRIX);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTrajet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedTrajet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Trajet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrajetUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTrajet, trajet), getPersistedTrajet(trajet));
    }

    @Test
    void fullUpdateTrajetWithPatch() throws Exception {
        // Initialize the database
        trajetRepository.save(trajet).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trajet using partial update
        Trajet partialUpdatedTrajet = new Trajet();
        partialUpdatedTrajet.setId(trajet.getId());

        partialUpdatedTrajet
            .depart(UPDATED_DEPART)
            .arrivee(UPDATED_ARRIVEE)
            .dateHeureDepart(UPDATED_DATE_HEURE_DEPART)
            .places(UPDATED_PLACES)
            .prix(UPDATED_PRIX);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTrajet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedTrajet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Trajet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrajetUpdatableFieldsEquals(partialUpdatedTrajet, getPersistedTrajet(partialUpdatedTrajet));
    }

    @Test
    void patchNonExistingTrajet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trajet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, trajet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Trajet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTrajet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trajet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Trajet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTrajet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trajet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(trajet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Trajet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTrajet() {
        // Initialize the database
        trajetRepository.save(trajet).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trajet
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, trajet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trajetRepository.count().block();
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

    protected Trajet getPersistedTrajet(Trajet trajet) {
        return trajetRepository.findById(trajet.getId()).block();
    }

    protected void assertPersistedTrajetToMatchAllProperties(Trajet expectedTrajet) {
        // Test fails because reactive api returns an empty object instead of null
        // assertTrajetAllPropertiesEquals(expectedTrajet, getPersistedTrajet(expectedTrajet));
        assertTrajetUpdatableFieldsEquals(expectedTrajet, getPersistedTrajet(expectedTrajet));
    }

    protected void assertPersistedTrajetToMatchUpdatableProperties(Trajet expectedTrajet) {
        // Test fails because reactive api returns an empty object instead of null
        // assertTrajetAllUpdatablePropertiesEquals(expectedTrajet, getPersistedTrajet(expectedTrajet));
        assertTrajetUpdatableFieldsEquals(expectedTrajet, getPersistedTrajet(expectedTrajet));
    }
}
