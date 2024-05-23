package com.gofind.gofind.web.rest;

import static com.gofind.gofind.domain.MaisonAsserts.*;
import static com.gofind.gofind.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofind.gofind.IntegrationTest;
import com.gofind.gofind.domain.Maison;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.MaisonRepository;
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
 * Integration tests for the {@link MaisonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MaisonResourceIT {

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/maisons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaisonRepository maisonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Maison maison;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maison createEntity(EntityManager em) {
        Maison maison = new Maison().adresse(DEFAULT_ADRESSE).description(DEFAULT_DESCRIPTION).image(DEFAULT_IMAGE);
        return maison;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maison createUpdatedEntity(EntityManager em) {
        Maison maison = new Maison().adresse(UPDATED_ADRESSE).description(UPDATED_DESCRIPTION).image(UPDATED_IMAGE);
        return maison;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Maison.class).block();
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
        maison = createEntity(em);
    }

    @Test
    void createMaison() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Maison
        var returnedMaison = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Maison.class)
            .returnResult()
            .getResponseBody();

        // Validate the Maison in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMaisonUpdatableFieldsEquals(returnedMaison, getPersistedMaison(returnedMaison));
    }

    @Test
    void createMaisonWithExistingId() throws Exception {
        // Create the Maison with an existing ID
        maison.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maison in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkAdresseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        maison.setAdresse(null);

        // Create the Maison, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMaisons() {
        // Initialize the database
        maisonRepository.save(maison).block();

        // Get all the maisonList
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
            .value(hasItem(maison.getId().intValue()))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].image")
            .value(hasItem(DEFAULT_IMAGE));
    }

    @Test
    void getMaison() {
        // Initialize the database
        maisonRepository.save(maison).block();

        // Get the maison
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, maison.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(maison.getId().intValue()))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.image")
            .value(is(DEFAULT_IMAGE));
    }

    @Test
    void getNonExistingMaison() {
        // Get the maison
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMaison() throws Exception {
        // Initialize the database
        maisonRepository.save(maison).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maison
        Maison updatedMaison = maisonRepository.findById(maison.getId()).block();
        updatedMaison.adresse(UPDATED_ADRESSE).description(UPDATED_DESCRIPTION).image(UPDATED_IMAGE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMaison.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedMaison))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Maison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaisonToMatchAllProperties(updatedMaison);
    }

    @Test
    void putNonExistingMaison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maison.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, maison.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMaison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maison.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMaison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maison.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Maison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMaisonWithPatch() throws Exception {
        // Initialize the database
        maisonRepository.save(maison).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maison using partial update
        Maison partialUpdatedMaison = new Maison();
        partialUpdatedMaison.setId(maison.getId());

        partialUpdatedMaison.image(UPDATED_IMAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMaison.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMaison))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Maison in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaisonUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMaison, maison), getPersistedMaison(maison));
    }

    @Test
    void fullUpdateMaisonWithPatch() throws Exception {
        // Initialize the database
        maisonRepository.save(maison).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maison using partial update
        Maison partialUpdatedMaison = new Maison();
        partialUpdatedMaison.setId(maison.getId());

        partialUpdatedMaison.adresse(UPDATED_ADRESSE).description(UPDATED_DESCRIPTION).image(UPDATED_IMAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMaison.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMaison))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Maison in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaisonUpdatableFieldsEquals(partialUpdatedMaison, getPersistedMaison(partialUpdatedMaison));
    }

    @Test
    void patchNonExistingMaison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maison.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, maison.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMaison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maison.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMaison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maison.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(maison))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Maison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMaison() {
        // Initialize the database
        maisonRepository.save(maison).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the maison
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, maison.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return maisonRepository.count().block();
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

    protected Maison getPersistedMaison(Maison maison) {
        return maisonRepository.findById(maison.getId()).block();
    }

    protected void assertPersistedMaisonToMatchAllProperties(Maison expectedMaison) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMaisonAllPropertiesEquals(expectedMaison, getPersistedMaison(expectedMaison));
        assertMaisonUpdatableFieldsEquals(expectedMaison, getPersistedMaison(expectedMaison));
    }

    protected void assertPersistedMaisonToMatchUpdatableProperties(Maison expectedMaison) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMaisonAllUpdatablePropertiesEquals(expectedMaison, getPersistedMaison(expectedMaison));
        assertMaisonUpdatableFieldsEquals(expectedMaison, getPersistedMaison(expectedMaison));
    }
}
