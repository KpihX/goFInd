package com.gofind.gofind.web.rest;

import static com.gofind.gofind.domain.UtilisateurAsserts.*;
import static com.gofind.gofind.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofind.gofind.IntegrationTest;
import com.gofind.gofind.domain.users.Utilisateur;
import com.gofind.gofind.repository.EntityManager;
import com.gofind.gofind.repository.users.UtilisateurRepository;
import com.gofind.gofind.service.users.UtilisateurService;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link UtilisateurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UtilisateurResourceIT {

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurRepository utilisateurRepositoryMock;

    @Mock
    private UtilisateurService utilisateurServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Utilisateur utilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur().telephone(DEFAULT_TELEPHONE);
        return utilisateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createUpdatedEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur().telephone(UPDATED_TELEPHONE);
        return utilisateur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_utilisateur__trajets").block();
            em.deleteAll(Utilisateur.class).block();
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
        utilisateur = createEntity(em);
    }

    @Test
    void createUtilisateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Utilisateur
        var returnedUtilisateur = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Utilisateur.class)
            .returnResult()
            .getResponseBody();

        // Validate the Utilisateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUtilisateurUpdatableFieldsEquals(returnedUtilisateur, getPersistedUtilisateur(returnedUtilisateur));
    }

    @Test
    void createUtilisateurWithExistingId() throws Exception {
        // Create the Utilisateur with an existing ID
        utilisateur.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTelephoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setTelephone(null);

        // Create the Utilisateur, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllUtilisateursAsStream() {
        // Initialize the database
        utilisateurRepository.save(utilisateur).block();

        List<Utilisateur> utilisateurList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Utilisateur.class)
            .getResponseBody()
            .filter(utilisateur::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(utilisateurList).isNotNull();
        assertThat(utilisateurList).hasSize(1);
        Utilisateur testUtilisateur = utilisateurList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertUtilisateurAllPropertiesEquals(utilisateur, testUtilisateur);
        assertUtilisateurUpdatableFieldsEquals(utilisateur, testUtilisateur);
    }

    @Test
    void getAllUtilisateurs() {
        // Initialize the database
        utilisateurRepository.save(utilisateur).block();

        // Get all the utilisateurList
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
            .value(hasItem(utilisateur.getId().intValue()))
            .jsonPath("$.[*].telephone")
            .value(hasItem(DEFAULT_TELEPHONE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisateursWithEagerRelationshipsIsEnabled() {
        when(utilisateurServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(utilisateurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisateursWithEagerRelationshipsIsNotEnabled() {
        when(utilisateurServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(utilisateurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUtilisateur() {
        // Initialize the database
        utilisateurRepository.save(utilisateur).block();

        // Get the utilisateur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, utilisateur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(utilisateur.getId().intValue()))
            .jsonPath("$.telephone")
            .value(is(DEFAULT_TELEPHONE));
    }

    @Test
    void getNonExistingUtilisateur() {
        // Get the utilisateur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.save(utilisateur).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(utilisateur.getId()).block();
        updatedUtilisateur.telephone(UPDATED_TELEPHONE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedUtilisateur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedUtilisateur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUtilisateurToMatchAllProperties(updatedUtilisateur);
    }

    @Test
    void putNonExistingUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, utilisateur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        utilisateurRepository.save(utilisateur).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur.telephone(UPDATED_TELEPHONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUtilisateur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Utilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUtilisateur, utilisateur),
            getPersistedUtilisateur(utilisateur)
        );
    }

    @Test
    void fullUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        utilisateurRepository.save(utilisateur).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur.telephone(UPDATED_TELEPHONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUtilisateur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Utilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurUpdatableFieldsEquals(partialUpdatedUtilisateur, getPersistedUtilisateur(partialUpdatedUtilisateur));
    }

    @Test
    void patchNonExistingUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, utilisateur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(utilisateur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUtilisateur() {
        // Initialize the database
        utilisateurRepository.save(utilisateur).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the utilisateur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, utilisateur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return utilisateurRepository.count().block();
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

    protected Utilisateur getPersistedUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.findById(utilisateur.getId()).block();
    }

    protected void assertPersistedUtilisateurToMatchAllProperties(Utilisateur expectedUtilisateur) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUtilisateurAllPropertiesEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
        assertUtilisateurUpdatableFieldsEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
    }

    protected void assertPersistedUtilisateurToMatchUpdatableProperties(Utilisateur expectedUtilisateur) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUtilisateurAllUpdatablePropertiesEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
        assertUtilisateurUpdatableFieldsEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
    }
}
