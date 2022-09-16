package fr.adaming.adp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.adaming.adp.IntegrationTest;
import fr.adaming.adp.domain.TauxDImposition;
import fr.adaming.adp.repository.TauxDImpositionRepository;
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
 * Integration tests for the {@link TauxDImpositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TauxDImpositionResourceIT {

    private static final Float DEFAULT_TAUX = 1F;
    private static final Float UPDATED_TAUX = 2F;

    private static final Integer DEFAULT_MIN_SALARY = 1;
    private static final Integer UPDATED_MIN_SALARY = 2;

    private static final Integer DEFAULT_MAX_SALARY = 1;
    private static final Integer UPDATED_MAX_SALARY = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/taux-d-impositions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TauxDImpositionRepository tauxDImpositionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTauxDImpositionMockMvc;

    private TauxDImposition tauxDImposition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TauxDImposition createEntity(EntityManager em) {
        TauxDImposition tauxDImposition = new TauxDImposition()
            .taux(DEFAULT_TAUX)
            .minSalary(DEFAULT_MIN_SALARY)
            .maxSalary(DEFAULT_MAX_SALARY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return tauxDImposition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TauxDImposition createUpdatedEntity(EntityManager em) {
        TauxDImposition tauxDImposition = new TauxDImposition()
            .taux(UPDATED_TAUX)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return tauxDImposition;
    }

    @BeforeEach
    public void initTest() {
        tauxDImposition = createEntity(em);
    }

    @Test
    @Transactional
    void createTauxDImposition() throws Exception {
        int databaseSizeBeforeCreate = tauxDImpositionRepository.findAll().size();
        // Create the TauxDImposition
        restTauxDImpositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isCreated());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeCreate + 1);
        TauxDImposition testTauxDImposition = tauxDImpositionList.get(tauxDImpositionList.size() - 1);
        assertThat(testTauxDImposition.getTaux()).isEqualTo(DEFAULT_TAUX);
        assertThat(testTauxDImposition.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testTauxDImposition.getMaxSalary()).isEqualTo(DEFAULT_MAX_SALARY);
        assertThat(testTauxDImposition.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTauxDImposition.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createTauxDImpositionWithExistingId() throws Exception {
        // Create the TauxDImposition with an existing ID
        tauxDImposition.setId(1L);

        int databaseSizeBeforeCreate = tauxDImpositionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTauxDImpositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTauxIsRequired() throws Exception {
        int databaseSizeBeforeTest = tauxDImpositionRepository.findAll().size();
        // set the field null
        tauxDImposition.setTaux(null);

        // Create the TauxDImposition, which fails.

        restTauxDImpositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isBadRequest());

        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinSalaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = tauxDImpositionRepository.findAll().size();
        // set the field null
        tauxDImposition.setMinSalary(null);

        // Create the TauxDImposition, which fails.

        restTauxDImpositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isBadRequest());

        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tauxDImpositionRepository.findAll().size();
        // set the field null
        tauxDImposition.setStartDate(null);

        // Create the TauxDImposition, which fails.

        restTauxDImpositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isBadRequest());

        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTauxDImpositions() throws Exception {
        // Initialize the database
        tauxDImpositionRepository.saveAndFlush(tauxDImposition);

        // Get all the tauxDImpositionList
        restTauxDImpositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tauxDImposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].taux").value(hasItem(DEFAULT_TAUX.doubleValue())))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY)))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(DEFAULT_MAX_SALARY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getTauxDImposition() throws Exception {
        // Initialize the database
        tauxDImpositionRepository.saveAndFlush(tauxDImposition);

        // Get the tauxDImposition
        restTauxDImpositionMockMvc
            .perform(get(ENTITY_API_URL_ID, tauxDImposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tauxDImposition.getId().intValue()))
            .andExpect(jsonPath("$.taux").value(DEFAULT_TAUX.doubleValue()))
            .andExpect(jsonPath("$.minSalary").value(DEFAULT_MIN_SALARY))
            .andExpect(jsonPath("$.maxSalary").value(DEFAULT_MAX_SALARY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTauxDImposition() throws Exception {
        // Get the tauxDImposition
        restTauxDImpositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTauxDImposition() throws Exception {
        // Initialize the database
        tauxDImpositionRepository.saveAndFlush(tauxDImposition);

        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();

        // Update the tauxDImposition
        TauxDImposition updatedTauxDImposition = tauxDImpositionRepository.findById(tauxDImposition.getId()).get();
        // Disconnect from session so that the updates on updatedTauxDImposition are not directly saved in db
        em.detach(updatedTauxDImposition);
        updatedTauxDImposition
            .taux(UPDATED_TAUX)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restTauxDImpositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTauxDImposition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTauxDImposition))
            )
            .andExpect(status().isOk());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
        TauxDImposition testTauxDImposition = tauxDImpositionList.get(tauxDImpositionList.size() - 1);
        assertThat(testTauxDImposition.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testTauxDImposition.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testTauxDImposition.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testTauxDImposition.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTauxDImposition.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTauxDImposition() throws Exception {
        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();
        tauxDImposition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTauxDImpositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tauxDImposition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTauxDImposition() throws Exception {
        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();
        tauxDImposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTauxDImpositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTauxDImposition() throws Exception {
        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();
        tauxDImposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTauxDImpositionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTauxDImpositionWithPatch() throws Exception {
        // Initialize the database
        tauxDImpositionRepository.saveAndFlush(tauxDImposition);

        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();

        // Update the tauxDImposition using partial update
        TauxDImposition partialUpdatedTauxDImposition = new TauxDImposition();
        partialUpdatedTauxDImposition.setId(tauxDImposition.getId());

        partialUpdatedTauxDImposition.taux(UPDATED_TAUX).maxSalary(UPDATED_MAX_SALARY);

        restTauxDImpositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTauxDImposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTauxDImposition))
            )
            .andExpect(status().isOk());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
        TauxDImposition testTauxDImposition = tauxDImpositionList.get(tauxDImpositionList.size() - 1);
        assertThat(testTauxDImposition.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testTauxDImposition.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testTauxDImposition.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testTauxDImposition.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTauxDImposition.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTauxDImpositionWithPatch() throws Exception {
        // Initialize the database
        tauxDImpositionRepository.saveAndFlush(tauxDImposition);

        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();

        // Update the tauxDImposition using partial update
        TauxDImposition partialUpdatedTauxDImposition = new TauxDImposition();
        partialUpdatedTauxDImposition.setId(tauxDImposition.getId());

        partialUpdatedTauxDImposition
            .taux(UPDATED_TAUX)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restTauxDImpositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTauxDImposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTauxDImposition))
            )
            .andExpect(status().isOk());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
        TauxDImposition testTauxDImposition = tauxDImpositionList.get(tauxDImpositionList.size() - 1);
        assertThat(testTauxDImposition.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testTauxDImposition.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testTauxDImposition.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testTauxDImposition.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTauxDImposition.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTauxDImposition() throws Exception {
        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();
        tauxDImposition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTauxDImpositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tauxDImposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTauxDImposition() throws Exception {
        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();
        tauxDImposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTauxDImpositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTauxDImposition() throws Exception {
        int databaseSizeBeforeUpdate = tauxDImpositionRepository.findAll().size();
        tauxDImposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTauxDImpositionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tauxDImposition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TauxDImposition in the database
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTauxDImposition() throws Exception {
        // Initialize the database
        tauxDImpositionRepository.saveAndFlush(tauxDImposition);

        int databaseSizeBeforeDelete = tauxDImpositionRepository.findAll().size();

        // Delete the tauxDImposition
        restTauxDImpositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tauxDImposition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TauxDImposition> tauxDImpositionList = tauxDImpositionRepository.findAll();
        assertThat(tauxDImpositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
