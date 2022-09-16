package fr.adaming.adp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.adaming.adp.IntegrationTest;
import fr.adaming.adp.domain.ConventionCollective;
import fr.adaming.adp.repository.ConventionCollectiveRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConventionCollectiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConventionCollectiveResourceIT {

    private static final Integer DEFAULT_IDCC = 1;
    private static final Integer UPDATED_IDCC = 2;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Float DEFAULT_POSITION = 1F;
    private static final Float UPDATED_POSITION = 2F;

    private static final Integer DEFAULT_COEFFICIENT = 1;
    private static final Integer UPDATED_COEFFICIENT = 2;

    private static final Float DEFAULT_VALEUR_POINT = 1F;
    private static final Float UPDATED_VALEUR_POINT = 2F;

    private static final Float DEFAULT_BASE_FIXE = 1F;
    private static final Float UPDATED_BASE_FIXE = 2F;

    private static final Float DEFAULT_SALAIRE_MINIMAUX = 1F;
    private static final Float UPDATED_SALAIRE_MINIMAUX = 2F;

    private static final String ENTITY_API_URL = "/api/convention-collectives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConventionCollectiveRepository conventionCollectiveRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConventionCollectiveMockMvc;

    private ConventionCollective conventionCollective;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConventionCollective createEntity(EntityManager em) {
        ConventionCollective conventionCollective = new ConventionCollective()
            .idcc(DEFAULT_IDCC)
            .nom(DEFAULT_NOM)
            .position(DEFAULT_POSITION)
            .coefficient(DEFAULT_COEFFICIENT)
            .valeurPoint(DEFAULT_VALEUR_POINT)
            .baseFixe(DEFAULT_BASE_FIXE)
            .salaireMinimaux(DEFAULT_SALAIRE_MINIMAUX);
        return conventionCollective;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConventionCollective createUpdatedEntity(EntityManager em) {
        ConventionCollective conventionCollective = new ConventionCollective()
            .idcc(UPDATED_IDCC)
            .nom(UPDATED_NOM)
            .position(UPDATED_POSITION)
            .coefficient(UPDATED_COEFFICIENT)
            .valeurPoint(UPDATED_VALEUR_POINT)
            .baseFixe(UPDATED_BASE_FIXE)
            .salaireMinimaux(UPDATED_SALAIRE_MINIMAUX);
        return conventionCollective;
    }

    @BeforeEach
    public void initTest() {
        conventionCollective = createEntity(em);
    }

    @Test
    @Transactional
    void createConventionCollective() throws Exception {
        int databaseSizeBeforeCreate = conventionCollectiveRepository.findAll().size();
        // Create the ConventionCollective
        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isCreated());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeCreate + 1);
        ConventionCollective testConventionCollective = conventionCollectiveList.get(conventionCollectiveList.size() - 1);
        assertThat(testConventionCollective.getIdcc()).isEqualTo(DEFAULT_IDCC);
        assertThat(testConventionCollective.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testConventionCollective.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testConventionCollective.getCoefficient()).isEqualTo(DEFAULT_COEFFICIENT);
        assertThat(testConventionCollective.getValeurPoint()).isEqualTo(DEFAULT_VALEUR_POINT);
        assertThat(testConventionCollective.getBaseFixe()).isEqualTo(DEFAULT_BASE_FIXE);
        assertThat(testConventionCollective.getSalaireMinimaux()).isEqualTo(DEFAULT_SALAIRE_MINIMAUX);
    }

    @Test
    @Transactional
    void createConventionCollectiveWithExistingId() throws Exception {
        // Create the ConventionCollective with an existing ID
        conventionCollective.setId(1L);

        int databaseSizeBeforeCreate = conventionCollectiveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdccIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionCollectiveRepository.findAll().size();
        // set the field null
        conventionCollective.setIdcc(null);

        // Create the ConventionCollective, which fails.

        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionCollectiveRepository.findAll().size();
        // set the field null
        conventionCollective.setNom(null);

        // Create the ConventionCollective, which fails.

        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionCollectiveRepository.findAll().size();
        // set the field null
        conventionCollective.setPosition(null);

        // Create the ConventionCollective, which fails.

        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoefficientIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionCollectiveRepository.findAll().size();
        // set the field null
        conventionCollective.setCoefficient(null);

        // Create the ConventionCollective, which fails.

        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValeurPointIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionCollectiveRepository.findAll().size();
        // set the field null
        conventionCollective.setValeurPoint(null);

        // Create the ConventionCollective, which fails.

        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBaseFixeIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionCollectiveRepository.findAll().size();
        // set the field null
        conventionCollective.setBaseFixe(null);

        // Create the ConventionCollective, which fails.

        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalaireMinimauxIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionCollectiveRepository.findAll().size();
        // set the field null
        conventionCollective.setSalaireMinimaux(null);

        // Create the ConventionCollective, which fails.

        restConventionCollectiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConventionCollectives() throws Exception {
        // Initialize the database
        conventionCollectiveRepository.saveAndFlush(conventionCollective);

        // Get all the conventionCollectiveList
        restConventionCollectiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conventionCollective.getId().intValue())))
            .andExpect(jsonPath("$.[*].idcc").value(hasItem(DEFAULT_IDCC)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.doubleValue())))
            .andExpect(jsonPath("$.[*].coefficient").value(hasItem(DEFAULT_COEFFICIENT)))
            .andExpect(jsonPath("$.[*].valeurPoint").value(hasItem(DEFAULT_VALEUR_POINT.doubleValue())))
            .andExpect(jsonPath("$.[*].baseFixe").value(hasItem(DEFAULT_BASE_FIXE.doubleValue())))
            .andExpect(jsonPath("$.[*].salaireMinimaux").value(hasItem(DEFAULT_SALAIRE_MINIMAUX.doubleValue())));
    }

    @Test
    @Transactional
    void getConventionCollective() throws Exception {
        // Initialize the database
        conventionCollectiveRepository.saveAndFlush(conventionCollective);

        // Get the conventionCollective
        restConventionCollectiveMockMvc
            .perform(get(ENTITY_API_URL_ID, conventionCollective.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conventionCollective.getId().intValue()))
            .andExpect(jsonPath("$.idcc").value(DEFAULT_IDCC))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.doubleValue()))
            .andExpect(jsonPath("$.coefficient").value(DEFAULT_COEFFICIENT))
            .andExpect(jsonPath("$.valeurPoint").value(DEFAULT_VALEUR_POINT.doubleValue()))
            .andExpect(jsonPath("$.baseFixe").value(DEFAULT_BASE_FIXE.doubleValue()))
            .andExpect(jsonPath("$.salaireMinimaux").value(DEFAULT_SALAIRE_MINIMAUX.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingConventionCollective() throws Exception {
        // Get the conventionCollective
        restConventionCollectiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConventionCollective() throws Exception {
        // Initialize the database
        conventionCollectiveRepository.saveAndFlush(conventionCollective);

        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();

        // Update the conventionCollective
        ConventionCollective updatedConventionCollective = conventionCollectiveRepository.findById(conventionCollective.getId()).get();
        // Disconnect from session so that the updates on updatedConventionCollective are not directly saved in db
        em.detach(updatedConventionCollective);
        updatedConventionCollective
            .idcc(UPDATED_IDCC)
            .nom(UPDATED_NOM)
            .position(UPDATED_POSITION)
            .coefficient(UPDATED_COEFFICIENT)
            .valeurPoint(UPDATED_VALEUR_POINT)
            .baseFixe(UPDATED_BASE_FIXE)
            .salaireMinimaux(UPDATED_SALAIRE_MINIMAUX);

        restConventionCollectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConventionCollective.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConventionCollective))
            )
            .andExpect(status().isOk());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
        ConventionCollective testConventionCollective = conventionCollectiveList.get(conventionCollectiveList.size() - 1);
        assertThat(testConventionCollective.getIdcc()).isEqualTo(UPDATED_IDCC);
        assertThat(testConventionCollective.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testConventionCollective.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testConventionCollective.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
        assertThat(testConventionCollective.getValeurPoint()).isEqualTo(UPDATED_VALEUR_POINT);
        assertThat(testConventionCollective.getBaseFixe()).isEqualTo(UPDATED_BASE_FIXE);
        assertThat(testConventionCollective.getSalaireMinimaux()).isEqualTo(UPDATED_SALAIRE_MINIMAUX);
    }

    @Test
    @Transactional
    void putNonExistingConventionCollective() throws Exception {
        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();
        conventionCollective.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConventionCollectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conventionCollective.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConventionCollective() throws Exception {
        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();
        conventionCollective.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionCollectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConventionCollective() throws Exception {
        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();
        conventionCollective.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionCollectiveMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConventionCollectiveWithPatch() throws Exception {
        // Initialize the database
        conventionCollectiveRepository.saveAndFlush(conventionCollective);

        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();

        // Update the conventionCollective using partial update
        ConventionCollective partialUpdatedConventionCollective = new ConventionCollective();
        partialUpdatedConventionCollective.setId(conventionCollective.getId());

        partialUpdatedConventionCollective
            .idcc(UPDATED_IDCC)
            .coefficient(UPDATED_COEFFICIENT)
            .valeurPoint(UPDATED_VALEUR_POINT)
            .salaireMinimaux(UPDATED_SALAIRE_MINIMAUX);

        restConventionCollectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConventionCollective.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConventionCollective))
            )
            .andExpect(status().isOk());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
        ConventionCollective testConventionCollective = conventionCollectiveList.get(conventionCollectiveList.size() - 1);
        assertThat(testConventionCollective.getIdcc()).isEqualTo(UPDATED_IDCC);
        assertThat(testConventionCollective.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testConventionCollective.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testConventionCollective.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
        assertThat(testConventionCollective.getValeurPoint()).isEqualTo(UPDATED_VALEUR_POINT);
        assertThat(testConventionCollective.getBaseFixe()).isEqualTo(DEFAULT_BASE_FIXE);
        assertThat(testConventionCollective.getSalaireMinimaux()).isEqualTo(UPDATED_SALAIRE_MINIMAUX);
    }

    @Test
    @Transactional
    void fullUpdateConventionCollectiveWithPatch() throws Exception {
        // Initialize the database
        conventionCollectiveRepository.saveAndFlush(conventionCollective);

        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();

        // Update the conventionCollective using partial update
        ConventionCollective partialUpdatedConventionCollective = new ConventionCollective();
        partialUpdatedConventionCollective.setId(conventionCollective.getId());

        partialUpdatedConventionCollective
            .idcc(UPDATED_IDCC)
            .nom(UPDATED_NOM)
            .position(UPDATED_POSITION)
            .coefficient(UPDATED_COEFFICIENT)
            .valeurPoint(UPDATED_VALEUR_POINT)
            .baseFixe(UPDATED_BASE_FIXE)
            .salaireMinimaux(UPDATED_SALAIRE_MINIMAUX);

        restConventionCollectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConventionCollective.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConventionCollective))
            )
            .andExpect(status().isOk());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
        ConventionCollective testConventionCollective = conventionCollectiveList.get(conventionCollectiveList.size() - 1);
        assertThat(testConventionCollective.getIdcc()).isEqualTo(UPDATED_IDCC);
        assertThat(testConventionCollective.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testConventionCollective.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testConventionCollective.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
        assertThat(testConventionCollective.getValeurPoint()).isEqualTo(UPDATED_VALEUR_POINT);
        assertThat(testConventionCollective.getBaseFixe()).isEqualTo(UPDATED_BASE_FIXE);
        assertThat(testConventionCollective.getSalaireMinimaux()).isEqualTo(UPDATED_SALAIRE_MINIMAUX);
    }

    @Test
    @Transactional
    void patchNonExistingConventionCollective() throws Exception {
        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();
        conventionCollective.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConventionCollectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conventionCollective.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConventionCollective() throws Exception {
        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();
        conventionCollective.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionCollectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConventionCollective() throws Exception {
        int databaseSizeBeforeUpdate = conventionCollectiveRepository.findAll().size();
        conventionCollective.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionCollectiveMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conventionCollective))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConventionCollective in the database
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConventionCollective() throws Exception {
        // Initialize the database
        conventionCollectiveRepository.saveAndFlush(conventionCollective);

        int databaseSizeBeforeDelete = conventionCollectiveRepository.findAll().size();

        // Delete the conventionCollective
        restConventionCollectiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, conventionCollective.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConventionCollective> conventionCollectiveList = conventionCollectiveRepository.findAll();
        assertThat(conventionCollectiveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
