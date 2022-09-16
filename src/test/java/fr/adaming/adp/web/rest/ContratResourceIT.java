package fr.adaming.adp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.adaming.adp.IntegrationTest;
import fr.adaming.adp.domain.Contrat;
import fr.adaming.adp.domain.enumeration.TypeForfait;
import fr.adaming.adp.repository.ContratRepository;
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
 * Integration tests for the {@link ContratResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContratResourceIT {

    private static final Float DEFAULT_SALAIRE_BASE = 1F;
    private static final Float UPDATED_SALAIRE_BASE = 2F;

    private static final String DEFAULT_EMPLOI = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOI = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ARRIVE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ARRIVE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_CLASSIFICATION = 1F;
    private static final Float UPDATED_CLASSIFICATION = 2F;

    private static final TypeForfait DEFAULT_TYPE_FORFAIT = TypeForfait.Jour;
    private static final TypeForfait UPDATED_TYPE_FORFAIT = TypeForfait.Heure;

    private static final Float DEFAULT_NB_HEURE = 1F;
    private static final Float UPDATED_NB_HEURE = 2F;

    private static final String ENTITY_API_URL = "/api/contrats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContratRepository contratRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContratMockMvc;

    private Contrat contrat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrat createEntity(EntityManager em) {
        Contrat contrat = new Contrat()
            .salaireBase(DEFAULT_SALAIRE_BASE)
            .emploi(DEFAULT_EMPLOI)
            .dateArrive(DEFAULT_DATE_ARRIVE)
            .classification(DEFAULT_CLASSIFICATION)
            .typeForfait(DEFAULT_TYPE_FORFAIT)
            .nbHeure(DEFAULT_NB_HEURE);
        return contrat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrat createUpdatedEntity(EntityManager em) {
        Contrat contrat = new Contrat()
            .salaireBase(UPDATED_SALAIRE_BASE)
            .emploi(UPDATED_EMPLOI)
            .dateArrive(UPDATED_DATE_ARRIVE)
            .classification(UPDATED_CLASSIFICATION)
            .typeForfait(UPDATED_TYPE_FORFAIT)
            .nbHeure(UPDATED_NB_HEURE);
        return contrat;
    }

    @BeforeEach
    public void initTest() {
        contrat = createEntity(em);
    }

    @Test
    @Transactional
    void createContrat() throws Exception {
        int databaseSizeBeforeCreate = contratRepository.findAll().size();
        // Create the Contrat
        restContratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isCreated());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeCreate + 1);
        Contrat testContrat = contratList.get(contratList.size() - 1);
        assertThat(testContrat.getSalaireBase()).isEqualTo(DEFAULT_SALAIRE_BASE);
        assertThat(testContrat.getEmploi()).isEqualTo(DEFAULT_EMPLOI);
        assertThat(testContrat.getDateArrive()).isEqualTo(DEFAULT_DATE_ARRIVE);
        assertThat(testContrat.getClassification()).isEqualTo(DEFAULT_CLASSIFICATION);
        assertThat(testContrat.getTypeForfait()).isEqualTo(DEFAULT_TYPE_FORFAIT);
        assertThat(testContrat.getNbHeure()).isEqualTo(DEFAULT_NB_HEURE);
    }

    @Test
    @Transactional
    void createContratWithExistingId() throws Exception {
        // Create the Contrat with an existing ID
        contrat.setId(1L);

        int databaseSizeBeforeCreate = contratRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSalaireBaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratRepository.findAll().size();
        // set the field null
        contrat.setSalaireBase(null);

        // Create the Contrat, which fails.

        restContratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isBadRequest());

        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmploiIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratRepository.findAll().size();
        // set the field null
        contrat.setEmploi(null);

        // Create the Contrat, which fails.

        restContratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isBadRequest());

        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateArriveIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratRepository.findAll().size();
        // set the field null
        contrat.setDateArrive(null);

        // Create the Contrat, which fails.

        restContratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isBadRequest());

        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratRepository.findAll().size();
        // set the field null
        contrat.setClassification(null);

        // Create the Contrat, which fails.

        restContratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isBadRequest());

        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeForfaitIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratRepository.findAll().size();
        // set the field null
        contrat.setTypeForfait(null);

        // Create the Contrat, which fails.

        restContratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isBadRequest());

        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContrats() throws Exception {
        // Initialize the database
        contratRepository.saveAndFlush(contrat);

        // Get all the contratList
        restContratMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contrat.getId().intValue())))
            .andExpect(jsonPath("$.[*].salaireBase").value(hasItem(DEFAULT_SALAIRE_BASE.doubleValue())))
            .andExpect(jsonPath("$.[*].emploi").value(hasItem(DEFAULT_EMPLOI)))
            .andExpect(jsonPath("$.[*].dateArrive").value(hasItem(DEFAULT_DATE_ARRIVE.toString())))
            .andExpect(jsonPath("$.[*].classification").value(hasItem(DEFAULT_CLASSIFICATION.doubleValue())))
            .andExpect(jsonPath("$.[*].typeForfait").value(hasItem(DEFAULT_TYPE_FORFAIT.toString())))
            .andExpect(jsonPath("$.[*].nbHeure").value(hasItem(DEFAULT_NB_HEURE.doubleValue())));
    }

    @Test
    @Transactional
    void getContrat() throws Exception {
        // Initialize the database
        contratRepository.saveAndFlush(contrat);

        // Get the contrat
        restContratMockMvc
            .perform(get(ENTITY_API_URL_ID, contrat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contrat.getId().intValue()))
            .andExpect(jsonPath("$.salaireBase").value(DEFAULT_SALAIRE_BASE.doubleValue()))
            .andExpect(jsonPath("$.emploi").value(DEFAULT_EMPLOI))
            .andExpect(jsonPath("$.dateArrive").value(DEFAULT_DATE_ARRIVE.toString()))
            .andExpect(jsonPath("$.classification").value(DEFAULT_CLASSIFICATION.doubleValue()))
            .andExpect(jsonPath("$.typeForfait").value(DEFAULT_TYPE_FORFAIT.toString()))
            .andExpect(jsonPath("$.nbHeure").value(DEFAULT_NB_HEURE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingContrat() throws Exception {
        // Get the contrat
        restContratMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContrat() throws Exception {
        // Initialize the database
        contratRepository.saveAndFlush(contrat);

        int databaseSizeBeforeUpdate = contratRepository.findAll().size();

        // Update the contrat
        Contrat updatedContrat = contratRepository.findById(contrat.getId()).get();
        // Disconnect from session so that the updates on updatedContrat are not directly saved in db
        em.detach(updatedContrat);
        updatedContrat
            .salaireBase(UPDATED_SALAIRE_BASE)
            .emploi(UPDATED_EMPLOI)
            .dateArrive(UPDATED_DATE_ARRIVE)
            .classification(UPDATED_CLASSIFICATION)
            .typeForfait(UPDATED_TYPE_FORFAIT)
            .nbHeure(UPDATED_NB_HEURE);

        restContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContrat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContrat))
            )
            .andExpect(status().isOk());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
        Contrat testContrat = contratList.get(contratList.size() - 1);
        assertThat(testContrat.getSalaireBase()).isEqualTo(UPDATED_SALAIRE_BASE);
        assertThat(testContrat.getEmploi()).isEqualTo(UPDATED_EMPLOI);
        assertThat(testContrat.getDateArrive()).isEqualTo(UPDATED_DATE_ARRIVE);
        assertThat(testContrat.getClassification()).isEqualTo(UPDATED_CLASSIFICATION);
        assertThat(testContrat.getTypeForfait()).isEqualTo(UPDATED_TYPE_FORFAIT);
        assertThat(testContrat.getNbHeure()).isEqualTo(UPDATED_NB_HEURE);
    }

    @Test
    @Transactional
    void putNonExistingContrat() throws Exception {
        int databaseSizeBeforeUpdate = contratRepository.findAll().size();
        contrat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contrat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contrat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContrat() throws Exception {
        int databaseSizeBeforeUpdate = contratRepository.findAll().size();
        contrat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contrat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContrat() throws Exception {
        int databaseSizeBeforeUpdate = contratRepository.findAll().size();
        contrat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContratWithPatch() throws Exception {
        // Initialize the database
        contratRepository.saveAndFlush(contrat);

        int databaseSizeBeforeUpdate = contratRepository.findAll().size();

        // Update the contrat using partial update
        Contrat partialUpdatedContrat = new Contrat();
        partialUpdatedContrat.setId(contrat.getId());

        partialUpdatedContrat.emploi(UPDATED_EMPLOI).typeForfait(UPDATED_TYPE_FORFAIT).nbHeure(UPDATED_NB_HEURE);

        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContrat))
            )
            .andExpect(status().isOk());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
        Contrat testContrat = contratList.get(contratList.size() - 1);
        assertThat(testContrat.getSalaireBase()).isEqualTo(DEFAULT_SALAIRE_BASE);
        assertThat(testContrat.getEmploi()).isEqualTo(UPDATED_EMPLOI);
        assertThat(testContrat.getDateArrive()).isEqualTo(DEFAULT_DATE_ARRIVE);
        assertThat(testContrat.getClassification()).isEqualTo(DEFAULT_CLASSIFICATION);
        assertThat(testContrat.getTypeForfait()).isEqualTo(UPDATED_TYPE_FORFAIT);
        assertThat(testContrat.getNbHeure()).isEqualTo(UPDATED_NB_HEURE);
    }

    @Test
    @Transactional
    void fullUpdateContratWithPatch() throws Exception {
        // Initialize the database
        contratRepository.saveAndFlush(contrat);

        int databaseSizeBeforeUpdate = contratRepository.findAll().size();

        // Update the contrat using partial update
        Contrat partialUpdatedContrat = new Contrat();
        partialUpdatedContrat.setId(contrat.getId());

        partialUpdatedContrat
            .salaireBase(UPDATED_SALAIRE_BASE)
            .emploi(UPDATED_EMPLOI)
            .dateArrive(UPDATED_DATE_ARRIVE)
            .classification(UPDATED_CLASSIFICATION)
            .typeForfait(UPDATED_TYPE_FORFAIT)
            .nbHeure(UPDATED_NB_HEURE);

        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContrat))
            )
            .andExpect(status().isOk());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
        Contrat testContrat = contratList.get(contratList.size() - 1);
        assertThat(testContrat.getSalaireBase()).isEqualTo(UPDATED_SALAIRE_BASE);
        assertThat(testContrat.getEmploi()).isEqualTo(UPDATED_EMPLOI);
        assertThat(testContrat.getDateArrive()).isEqualTo(UPDATED_DATE_ARRIVE);
        assertThat(testContrat.getClassification()).isEqualTo(UPDATED_CLASSIFICATION);
        assertThat(testContrat.getTypeForfait()).isEqualTo(UPDATED_TYPE_FORFAIT);
        assertThat(testContrat.getNbHeure()).isEqualTo(UPDATED_NB_HEURE);
    }

    @Test
    @Transactional
    void patchNonExistingContrat() throws Exception {
        int databaseSizeBeforeUpdate = contratRepository.findAll().size();
        contrat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contrat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contrat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContrat() throws Exception {
        int databaseSizeBeforeUpdate = contratRepository.findAll().size();
        contrat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contrat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContrat() throws Exception {
        int databaseSizeBeforeUpdate = contratRepository.findAll().size();
        contrat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contrat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrat in the database
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContrat() throws Exception {
        // Initialize the database
        contratRepository.saveAndFlush(contrat);

        int databaseSizeBeforeDelete = contratRepository.findAll().size();

        // Delete the contrat
        restContratMockMvc
            .perform(delete(ENTITY_API_URL_ID, contrat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contrat> contratList = contratRepository.findAll();
        assertThat(contratList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
