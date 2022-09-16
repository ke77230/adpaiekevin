package fr.adaming.adp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.adaming.adp.IntegrationTest;
import fr.adaming.adp.domain.Conge;
import fr.adaming.adp.domain.enumeration.Decision;
import fr.adaming.adp.repository.CongeRepository;
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
 * Integration tests for the {@link CongeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CongeResourceIT {

    private static final LocalDate DEFAULT_HOLDATE_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HOLDATE_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_HOLDATE_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HOLDATE_END = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_HOLDATE_PAY = 1F;
    private static final Float UPDATED_HOLDATE_PAY = 2F;

    private static final Float DEFAULT_NB_CONGE_ACQUIS = 1F;
    private static final Float UPDATED_NB_CONGE_ACQUIS = 2F;

    private static final Float DEFAULT_NB_CONGE_PRIS = 1F;
    private static final Float UPDATED_NB_CONGE_PRIS = 2F;

    private static final LocalDate DEFAULT_DATE_DEMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMANDE = LocalDate.now(ZoneId.systemDefault());

    private static final Decision DEFAULT_DECISION = Decision.Accepte;
    private static final Decision UPDATED_DECISION = Decision.Refuse;

    private static final LocalDate DEFAULT_DATE_REPONSE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REPONSE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/conges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CongeRepository congeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCongeMockMvc;

    private Conge conge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conge createEntity(EntityManager em) {
        Conge conge = new Conge()
            .holdateStart(DEFAULT_HOLDATE_START)
            .holdateEnd(DEFAULT_HOLDATE_END)
            .holdatePay(DEFAULT_HOLDATE_PAY)
            .nbCongeAcquis(DEFAULT_NB_CONGE_ACQUIS)
            .nbCongePris(DEFAULT_NB_CONGE_PRIS)
            .dateDemande(DEFAULT_DATE_DEMANDE)
            .decision(DEFAULT_DECISION)
            .dateReponse(DEFAULT_DATE_REPONSE);
        return conge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conge createUpdatedEntity(EntityManager em) {
        Conge conge = new Conge()
            .holdateStart(UPDATED_HOLDATE_START)
            .holdateEnd(UPDATED_HOLDATE_END)
            .holdatePay(UPDATED_HOLDATE_PAY)
            .nbCongeAcquis(UPDATED_NB_CONGE_ACQUIS)
            .nbCongePris(UPDATED_NB_CONGE_PRIS)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .decision(UPDATED_DECISION)
            .dateReponse(UPDATED_DATE_REPONSE);
        return conge;
    }

    @BeforeEach
    public void initTest() {
        conge = createEntity(em);
    }

    @Test
    @Transactional
    void createConge() throws Exception {
        int databaseSizeBeforeCreate = congeRepository.findAll().size();
        // Create the Conge
        restCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isCreated());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeCreate + 1);
        Conge testConge = congeList.get(congeList.size() - 1);
        assertThat(testConge.getHoldateStart()).isEqualTo(DEFAULT_HOLDATE_START);
        assertThat(testConge.getHoldateEnd()).isEqualTo(DEFAULT_HOLDATE_END);
        assertThat(testConge.getHoldatePay()).isEqualTo(DEFAULT_HOLDATE_PAY);
        assertThat(testConge.getNbCongeAcquis()).isEqualTo(DEFAULT_NB_CONGE_ACQUIS);
        assertThat(testConge.getNbCongePris()).isEqualTo(DEFAULT_NB_CONGE_PRIS);
        assertThat(testConge.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testConge.getDecision()).isEqualTo(DEFAULT_DECISION);
        assertThat(testConge.getDateReponse()).isEqualTo(DEFAULT_DATE_REPONSE);
    }

    @Test
    @Transactional
    void createCongeWithExistingId() throws Exception {
        // Create the Conge with an existing ID
        conge.setId(1L);

        int databaseSizeBeforeCreate = congeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isBadRequest());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHoldateStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = congeRepository.findAll().size();
        // set the field null
        conge.setHoldateStart(null);

        // Create the Conge, which fails.

        restCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isBadRequest());

        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoldateEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = congeRepository.findAll().size();
        // set the field null
        conge.setHoldateEnd(null);

        // Create the Conge, which fails.

        restCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isBadRequest());

        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoldatePayIsRequired() throws Exception {
        int databaseSizeBeforeTest = congeRepository.findAll().size();
        // set the field null
        conge.setHoldatePay(null);

        // Create the Conge, which fails.

        restCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isBadRequest());

        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbCongePrisIsRequired() throws Exception {
        int databaseSizeBeforeTest = congeRepository.findAll().size();
        // set the field null
        conge.setNbCongePris(null);

        // Create the Conge, which fails.

        restCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isBadRequest());

        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDemandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = congeRepository.findAll().size();
        // set the field null
        conge.setDateDemande(null);

        // Create the Conge, which fails.

        restCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isBadRequest());

        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = congeRepository.findAll().size();
        // set the field null
        conge.setDecision(null);

        // Create the Conge, which fails.

        restCongeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isBadRequest());

        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConges() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);

        // Get all the congeList
        restCongeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conge.getId().intValue())))
            .andExpect(jsonPath("$.[*].holdateStart").value(hasItem(DEFAULT_HOLDATE_START.toString())))
            .andExpect(jsonPath("$.[*].holdateEnd").value(hasItem(DEFAULT_HOLDATE_END.toString())))
            .andExpect(jsonPath("$.[*].holdatePay").value(hasItem(DEFAULT_HOLDATE_PAY.doubleValue())))
            .andExpect(jsonPath("$.[*].nbCongeAcquis").value(hasItem(DEFAULT_NB_CONGE_ACQUIS.doubleValue())))
            .andExpect(jsonPath("$.[*].nbCongePris").value(hasItem(DEFAULT_NB_CONGE_PRIS.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())))
            .andExpect(jsonPath("$.[*].dateReponse").value(hasItem(DEFAULT_DATE_REPONSE.toString())));
    }

    @Test
    @Transactional
    void getConge() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);

        // Get the conge
        restCongeMockMvc
            .perform(get(ENTITY_API_URL_ID, conge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conge.getId().intValue()))
            .andExpect(jsonPath("$.holdateStart").value(DEFAULT_HOLDATE_START.toString()))
            .andExpect(jsonPath("$.holdateEnd").value(DEFAULT_HOLDATE_END.toString()))
            .andExpect(jsonPath("$.holdatePay").value(DEFAULT_HOLDATE_PAY.doubleValue()))
            .andExpect(jsonPath("$.nbCongeAcquis").value(DEFAULT_NB_CONGE_ACQUIS.doubleValue()))
            .andExpect(jsonPath("$.nbCongePris").value(DEFAULT_NB_CONGE_PRIS.doubleValue()))
            .andExpect(jsonPath("$.dateDemande").value(DEFAULT_DATE_DEMANDE.toString()))
            .andExpect(jsonPath("$.decision").value(DEFAULT_DECISION.toString()))
            .andExpect(jsonPath("$.dateReponse").value(DEFAULT_DATE_REPONSE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingConge() throws Exception {
        // Get the conge
        restCongeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConge() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);

        int databaseSizeBeforeUpdate = congeRepository.findAll().size();

        // Update the conge
        Conge updatedConge = congeRepository.findById(conge.getId()).get();
        // Disconnect from session so that the updates on updatedConge are not directly saved in db
        em.detach(updatedConge);
        updatedConge
            .holdateStart(UPDATED_HOLDATE_START)
            .holdateEnd(UPDATED_HOLDATE_END)
            .holdatePay(UPDATED_HOLDATE_PAY)
            .nbCongeAcquis(UPDATED_NB_CONGE_ACQUIS)
            .nbCongePris(UPDATED_NB_CONGE_PRIS)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .decision(UPDATED_DECISION)
            .dateReponse(UPDATED_DATE_REPONSE);

        restCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConge))
            )
            .andExpect(status().isOk());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
        Conge testConge = congeList.get(congeList.size() - 1);
        assertThat(testConge.getHoldateStart()).isEqualTo(UPDATED_HOLDATE_START);
        assertThat(testConge.getHoldateEnd()).isEqualTo(UPDATED_HOLDATE_END);
        assertThat(testConge.getHoldatePay()).isEqualTo(UPDATED_HOLDATE_PAY);
        assertThat(testConge.getNbCongeAcquis()).isEqualTo(UPDATED_NB_CONGE_ACQUIS);
        assertThat(testConge.getNbCongePris()).isEqualTo(UPDATED_NB_CONGE_PRIS);
        assertThat(testConge.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testConge.getDecision()).isEqualTo(UPDATED_DECISION);
        assertThat(testConge.getDateReponse()).isEqualTo(UPDATED_DATE_REPONSE);
    }

    @Test
    @Transactional
    void putNonExistingConge() throws Exception {
        int databaseSizeBeforeUpdate = congeRepository.findAll().size();
        conge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConge() throws Exception {
        int databaseSizeBeforeUpdate = congeRepository.findAll().size();
        conge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCongeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConge() throws Exception {
        int databaseSizeBeforeUpdate = congeRepository.findAll().size();
        conge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCongeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCongeWithPatch() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);

        int databaseSizeBeforeUpdate = congeRepository.findAll().size();

        // Update the conge using partial update
        Conge partialUpdatedConge = new Conge();
        partialUpdatedConge.setId(conge.getId());

        partialUpdatedConge.holdateEnd(UPDATED_HOLDATE_END).nbCongeAcquis(UPDATED_NB_CONGE_ACQUIS).dateDemande(UPDATED_DATE_DEMANDE);

        restCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConge))
            )
            .andExpect(status().isOk());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
        Conge testConge = congeList.get(congeList.size() - 1);
        assertThat(testConge.getHoldateStart()).isEqualTo(DEFAULT_HOLDATE_START);
        assertThat(testConge.getHoldateEnd()).isEqualTo(UPDATED_HOLDATE_END);
        assertThat(testConge.getHoldatePay()).isEqualTo(DEFAULT_HOLDATE_PAY);
        assertThat(testConge.getNbCongeAcquis()).isEqualTo(UPDATED_NB_CONGE_ACQUIS);
        assertThat(testConge.getNbCongePris()).isEqualTo(DEFAULT_NB_CONGE_PRIS);
        assertThat(testConge.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testConge.getDecision()).isEqualTo(DEFAULT_DECISION);
        assertThat(testConge.getDateReponse()).isEqualTo(DEFAULT_DATE_REPONSE);
    }

    @Test
    @Transactional
    void fullUpdateCongeWithPatch() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);

        int databaseSizeBeforeUpdate = congeRepository.findAll().size();

        // Update the conge using partial update
        Conge partialUpdatedConge = new Conge();
        partialUpdatedConge.setId(conge.getId());

        partialUpdatedConge
            .holdateStart(UPDATED_HOLDATE_START)
            .holdateEnd(UPDATED_HOLDATE_END)
            .holdatePay(UPDATED_HOLDATE_PAY)
            .nbCongeAcquis(UPDATED_NB_CONGE_ACQUIS)
            .nbCongePris(UPDATED_NB_CONGE_PRIS)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .decision(UPDATED_DECISION)
            .dateReponse(UPDATED_DATE_REPONSE);

        restCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConge))
            )
            .andExpect(status().isOk());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
        Conge testConge = congeList.get(congeList.size() - 1);
        assertThat(testConge.getHoldateStart()).isEqualTo(UPDATED_HOLDATE_START);
        assertThat(testConge.getHoldateEnd()).isEqualTo(UPDATED_HOLDATE_END);
        assertThat(testConge.getHoldatePay()).isEqualTo(UPDATED_HOLDATE_PAY);
        assertThat(testConge.getNbCongeAcquis()).isEqualTo(UPDATED_NB_CONGE_ACQUIS);
        assertThat(testConge.getNbCongePris()).isEqualTo(UPDATED_NB_CONGE_PRIS);
        assertThat(testConge.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testConge.getDecision()).isEqualTo(UPDATED_DECISION);
        assertThat(testConge.getDateReponse()).isEqualTo(UPDATED_DATE_REPONSE);
    }

    @Test
    @Transactional
    void patchNonExistingConge() throws Exception {
        int databaseSizeBeforeUpdate = congeRepository.findAll().size();
        conge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConge() throws Exception {
        int databaseSizeBeforeUpdate = congeRepository.findAll().size();
        conge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCongeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConge() throws Exception {
        int databaseSizeBeforeUpdate = congeRepository.findAll().size();
        conge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCongeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConge() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);

        int databaseSizeBeforeDelete = congeRepository.findAll().size();

        // Delete the conge
        restCongeMockMvc
            .perform(delete(ENTITY_API_URL_ID, conge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
