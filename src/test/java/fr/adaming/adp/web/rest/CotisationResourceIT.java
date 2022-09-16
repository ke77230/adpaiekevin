package fr.adaming.adp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.adaming.adp.IntegrationTest;
import fr.adaming.adp.domain.Cotisation;
import fr.adaming.adp.domain.enumeration.Categorie;
import fr.adaming.adp.repository.CotisationRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CotisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CotisationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Categorie DEFAULT_FAMILLE = Categorie.Sante;
    private static final Categorie UPDATED_FAMILLE = Categorie.Retraite;

    private static final Float DEFAULT_TAUX = 1F;
    private static final Float UPDATED_TAUX = 2F;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTUEL = false;
    private static final Boolean UPDATED_ACTUEL = true;

    private static final String ENTITY_API_URL = "/api/cotisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CotisationRepository cotisationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCotisationMockMvc;

    private Cotisation cotisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cotisation createEntity(EntityManager em) {
        Cotisation cotisation = new Cotisation()
            .name(DEFAULT_NAME)
            .famille(DEFAULT_FAMILLE)
            .taux(DEFAULT_TAUX)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .actuel(DEFAULT_ACTUEL);
        return cotisation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cotisation createUpdatedEntity(EntityManager em) {
        Cotisation cotisation = new Cotisation()
            .name(UPDATED_NAME)
            .famille(UPDATED_FAMILLE)
            .taux(UPDATED_TAUX)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .actuel(UPDATED_ACTUEL);
        return cotisation;
    }

    @BeforeEach
    public void initTest() {
        cotisation = createEntity(em);
    }

    @Test
    @Transactional
    void createCotisation() throws Exception {
        int databaseSizeBeforeCreate = cotisationRepository.findAll().size();
        // Create the Cotisation
        restCotisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisation)))
            .andExpect(status().isCreated());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeCreate + 1);
        Cotisation testCotisation = cotisationList.get(cotisationList.size() - 1);
        assertThat(testCotisation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCotisation.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testCotisation.getTaux()).isEqualTo(DEFAULT_TAUX);
        assertThat(testCotisation.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCotisation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCotisation.getActuel()).isEqualTo(DEFAULT_ACTUEL);
    }

    @Test
    @Transactional
    void createCotisationWithExistingId() throws Exception {
        // Create the Cotisation with an existing ID
        cotisation.setId(1L);

        int databaseSizeBeforeCreate = cotisationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCotisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisation)))
            .andExpect(status().isBadRequest());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cotisationRepository.findAll().size();
        // set the field null
        cotisation.setName(null);

        // Create the Cotisation, which fails.

        restCotisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisation)))
            .andExpect(status().isBadRequest());

        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = cotisationRepository.findAll().size();
        // set the field null
        cotisation.setFamille(null);

        // Create the Cotisation, which fails.

        restCotisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisation)))
            .andExpect(status().isBadRequest());

        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTauxIsRequired() throws Exception {
        int databaseSizeBeforeTest = cotisationRepository.findAll().size();
        // set the field null
        cotisation.setTaux(null);

        // Create the Cotisation, which fails.

        restCotisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisation)))
            .andExpect(status().isBadRequest());

        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cotisationRepository.findAll().size();
        // set the field null
        cotisation.setStartDate(null);

        // Create the Cotisation, which fails.

        restCotisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisation)))
            .andExpect(status().isBadRequest());

        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActuelIsRequired() throws Exception {
        int databaseSizeBeforeTest = cotisationRepository.findAll().size();
        // set the field null
        cotisation.setActuel(null);

        // Create the Cotisation, which fails.

        restCotisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisation)))
            .andExpect(status().isBadRequest());

        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCotisations() throws Exception {
        // Initialize the database
        cotisationRepository.saveAndFlush(cotisation);

        // Get all the cotisationList
        restCotisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cotisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE.toString())))
            .andExpect(jsonPath("$.[*].taux").value(hasItem(DEFAULT_TAUX.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].actuel").value(hasItem(DEFAULT_ACTUEL.booleanValue())));
    }

    @Test
    @Transactional
    void getCotisation() throws Exception {
        // Initialize the database
        cotisationRepository.saveAndFlush(cotisation);

        // Get the cotisation
        restCotisationMockMvc
            .perform(get(ENTITY_API_URL_ID, cotisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cotisation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE.toString()))
            .andExpect(jsonPath("$.taux").value(DEFAULT_TAUX.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.actuel").value(DEFAULT_ACTUEL.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCotisation() throws Exception {
        // Get the cotisation
        restCotisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCotisation() throws Exception {
        // Initialize the database
        cotisationRepository.saveAndFlush(cotisation);

        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();

        // Update the cotisation
        Cotisation updatedCotisation = cotisationRepository.findById(cotisation.getId()).get();
        // Disconnect from session so that the updates on updatedCotisation are not directly saved in db
        em.detach(updatedCotisation);
        updatedCotisation
            .name(UPDATED_NAME)
            .famille(UPDATED_FAMILLE)
            .taux(UPDATED_TAUX)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .actuel(UPDATED_ACTUEL);

        restCotisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCotisation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCotisation))
            )
            .andExpect(status().isOk());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
        Cotisation testCotisation = cotisationList.get(cotisationList.size() - 1);
        assertThat(testCotisation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCotisation.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCotisation.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testCotisation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCotisation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCotisation.getActuel()).isEqualTo(UPDATED_ACTUEL);
    }

    @Test
    @Transactional
    void putNonExistingCotisation() throws Exception {
        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();
        cotisation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCotisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cotisation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCotisation() throws Exception {
        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();
        cotisation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCotisation() throws Exception {
        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();
        cotisation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCotisationWithPatch() throws Exception {
        // Initialize the database
        cotisationRepository.saveAndFlush(cotisation);

        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();

        // Update the cotisation using partial update
        Cotisation partialUpdatedCotisation = new Cotisation();
        partialUpdatedCotisation.setId(cotisation.getId());

        partialUpdatedCotisation.famille(UPDATED_FAMILLE).taux(UPDATED_TAUX).startDate(UPDATED_START_DATE).actuel(UPDATED_ACTUEL);

        restCotisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCotisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCotisation))
            )
            .andExpect(status().isOk());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
        Cotisation testCotisation = cotisationList.get(cotisationList.size() - 1);
        assertThat(testCotisation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCotisation.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCotisation.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testCotisation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCotisation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCotisation.getActuel()).isEqualTo(UPDATED_ACTUEL);
    }

    @Test
    @Transactional
    void fullUpdateCotisationWithPatch() throws Exception {
        // Initialize the database
        cotisationRepository.saveAndFlush(cotisation);

        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();

        // Update the cotisation using partial update
        Cotisation partialUpdatedCotisation = new Cotisation();
        partialUpdatedCotisation.setId(cotisation.getId());

        partialUpdatedCotisation
            .name(UPDATED_NAME)
            .famille(UPDATED_FAMILLE)
            .taux(UPDATED_TAUX)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .actuel(UPDATED_ACTUEL);

        restCotisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCotisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCotisation))
            )
            .andExpect(status().isOk());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
        Cotisation testCotisation = cotisationList.get(cotisationList.size() - 1);
        assertThat(testCotisation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCotisation.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCotisation.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testCotisation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCotisation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCotisation.getActuel()).isEqualTo(UPDATED_ACTUEL);
    }

    @Test
    @Transactional
    void patchNonExistingCotisation() throws Exception {
        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();
        cotisation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCotisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cotisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cotisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCotisation() throws Exception {
        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();
        cotisation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cotisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCotisation() throws Exception {
        int databaseSizeBeforeUpdate = cotisationRepository.findAll().size();
        cotisation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cotisation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cotisation in the database
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCotisation() throws Exception {
        // Initialize the database
        cotisationRepository.saveAndFlush(cotisation);

        int databaseSizeBeforeDelete = cotisationRepository.findAll().size();

        // Delete the cotisation
        restCotisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, cotisation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cotisation> cotisationList = cotisationRepository.findAll();
        assertThat(cotisationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
