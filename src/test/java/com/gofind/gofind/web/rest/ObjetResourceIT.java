package com.gofind.gofind.web.rest;

import static com.gofind.gofind.domain.ObjetAsserts.*;
import static com.gofind.gofind.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofind.gofind.IntegrationTest;
import com.gofind.gofind.domain.enumeration.EtatObjet;
import com.gofind.gofind.domain.enumeration.TypeObjet;
import com.gofind.gofind.domain.objects.Objet;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.objects.ObjetRepository;
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
 * Integration tests for the {@link ObjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ObjetResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TypeObjet DEFAULT_TYPE = TypeObjet.TELEPHONE;
    private static final TypeObjet UPDATED_TYPE = TypeObjet.LAPTOP;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFIANT = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIANT = "BBBBBBBBBB";

    private static final EtatObjet DEFAULT_ETAT = EtatObjet.VOLE;
    private static final EtatObjet UPDATED_ETAT = EtatObjet.RETROUVE;

    private static final String ENTITY_API_URL = "/api/objets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ObjetRepository objetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Objet objet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objet createEntity(EntityManager em) {
        Objet objet = new Objet()
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .image(DEFAULT_IMAGE)
            .identifiant(DEFAULT_IDENTIFIANT)
            .etat(DEFAULT_ETAT);
        return objet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objet createUpdatedEntity(EntityManager em) {
        Objet objet = new Objet()
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .image(UPDATED_IMAGE)
            .identifiant(UPDATED_IDENTIFIANT)
            .etat(UPDATED_ETAT);
        return objet;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Objet.class).block();
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
        objet = createEntity(em);
    }

    @Test
    void createObjet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Objet
        var returnedObjet = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Objet.class)
            .returnResult()
            .getResponseBody();

        // Validate the Objet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertObjetUpdatableFieldsEquals(returnedObjet, getPersistedObjet(returnedObjet));
    }

    @Test
    void createObjetWithExistingId() throws Exception {
        // Create the Objet with an existing ID
        objet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Objet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        objet.setLibelle(null);

        // Create the Objet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        objet.setType(null);

        // Create the Objet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIdentifiantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        objet.setIdentifiant(null);

        // Create the Objet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEtatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        objet.setEtat(null);

        // Create the Objet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllObjets() {
        // Initialize the database
        objetRepository.save(objet).block();

        // Get all the objetList
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
            .value(hasItem(objet.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].image")
            .value(hasItem(DEFAULT_IMAGE))
            .jsonPath("$.[*].identifiant")
            .value(hasItem(DEFAULT_IDENTIFIANT))
            .jsonPath("$.[*].etat")
            .value(hasItem(DEFAULT_ETAT.toString()));
    }

    @Test
    void getObjet() {
        // Initialize the database
        objetRepository.save(objet).block();

        // Get the objet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, objet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(objet.getId().intValue()))
            .jsonPath("$.libelle")
            .value(is(DEFAULT_LIBELLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.image")
            .value(is(DEFAULT_IMAGE))
            .jsonPath("$.identifiant")
            .value(is(DEFAULT_IDENTIFIANT))
            .jsonPath("$.etat")
            .value(is(DEFAULT_ETAT.toString()));
    }

    @Test
    void getNonExistingObjet() {
        // Get the objet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingObjet() throws Exception {
        // Initialize the database
        objetRepository.save(objet).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the objet
        Objet updatedObjet = objetRepository.findById(objet.getId()).block();
        updatedObjet
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .image(UPDATED_IMAGE)
            .identifiant(UPDATED_IDENTIFIANT)
            .etat(UPDATED_ETAT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedObjet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedObjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Objet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedObjetToMatchAllProperties(updatedObjet);
    }

    @Test
    void putNonExistingObjet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, objet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Objet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchObjet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Objet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamObjet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Objet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateObjetWithPatch() throws Exception {
        // Initialize the database
        objetRepository.save(objet).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the objet using partial update
        Objet partialUpdatedObjet = new Objet();
        partialUpdatedObjet.setId(objet.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedObjet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedObjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Objet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertObjetUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedObjet, objet), getPersistedObjet(objet));
    }

    @Test
    void fullUpdateObjetWithPatch() throws Exception {
        // Initialize the database
        objetRepository.save(objet).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the objet using partial update
        Objet partialUpdatedObjet = new Objet();
        partialUpdatedObjet.setId(objet.getId());

        partialUpdatedObjet
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .image(UPDATED_IMAGE)
            .identifiant(UPDATED_IDENTIFIANT)
            .etat(UPDATED_ETAT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedObjet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedObjet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Objet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertObjetUpdatableFieldsEquals(partialUpdatedObjet, getPersistedObjet(partialUpdatedObjet));
    }

    @Test
    void patchNonExistingObjet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, objet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Objet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchObjet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Objet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamObjet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(objet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Objet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteObjet() {
        // Initialize the database
        objetRepository.save(objet).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the objet
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, objet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return objetRepository.count().block();
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

    protected Objet getPersistedObjet(Objet objet) {
        return objetRepository.findById(objet.getId()).block();
    }

    protected void assertPersistedObjetToMatchAllProperties(Objet expectedObjet) {
        // Test fails because reactive api returns an empty object instead of null
        // assertObjetAllPropertiesEquals(expectedObjet, getPersistedObjet(expectedObjet));
        assertObjetUpdatableFieldsEquals(expectedObjet, getPersistedObjet(expectedObjet));
    }

    protected void assertPersistedObjetToMatchUpdatableProperties(Objet expectedObjet) {
        // Test fails because reactive api returns an empty object instead of null
        // assertObjetAllUpdatablePropertiesEquals(expectedObjet, getPersistedObjet(expectedObjet));
        assertObjetUpdatableFieldsEquals(expectedObjet, getPersistedObjet(expectedObjet));
    }
}
