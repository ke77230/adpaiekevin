package fr.adaming.adp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.adaming.adp.IntegrationTest;
import fr.adaming.adp.domain.FicheDePaie;
import fr.adaming.adp.repository.FicheDePaieRepository;
import fr.adaming.adp.service.FicheDePaieService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FicheDePaieResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FicheDePaieResourceIT {

    private static final Float DEFAULT_SALAIRE_BRUT = 1F;
    private static final Float UPDATED_SALAIRE_BRUT = 2F;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATEPAIEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEPAIEMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_SALAIRE_NET = 1F;
    private static final Float UPDATED_SALAIRE_NET = 2F;

    private static final Float DEFAULT_MONTANT_NET_AVANT_IMPOTS = 1F;
    private static final Float UPDATED_MONTANT_NET_AVANT_IMPOTS = 2F;

    private static final Float DEFAULT_PRO_FEES = 1F;
    private static final Float UPDATED_PRO_FEES = 2F;

    private static final Float DEFAULT_DEDUCTIONS = 1F;
    private static final Float UPDATED_DEDUCTIONS = 2F;

    private static final String ENTITY_API_URL = "/api/fiche-de-paies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FicheDePaieRepository ficheDePaieRepository;

    @Mock
    private FicheDePaieRepository ficheDePaieRepositoryMock;

    @Mock
    private FicheDePaieService ficheDePaieServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFicheDePaieMockMvc;

    private FicheDePaie ficheDePaie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FicheDePaie createEntity(EntityManager em) {
        FicheDePaie ficheDePaie = new FicheDePaie()
            .salaireBrut(DEFAULT_SALAIRE_BRUT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .datepaiement(DEFAULT_DATEPAIEMENT)
            .salaireNet(DEFAULT_SALAIRE_NET)
            .montantNetAvantImpots(DEFAULT_MONTANT_NET_AVANT_IMPOTS)
            .proFees(DEFAULT_PRO_FEES)
            .deductions(DEFAULT_DEDUCTIONS);
        return ficheDePaie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FicheDePaie createUpdatedEntity(EntityManager em) {
        FicheDePaie ficheDePaie = new FicheDePaie()
            .salaireBrut(UPDATED_SALAIRE_BRUT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .datepaiement(UPDATED_DATEPAIEMENT)
            .salaireNet(UPDATED_SALAIRE_NET)
            .montantNetAvantImpots(UPDATED_MONTANT_NET_AVANT_IMPOTS)
            .proFees(UPDATED_PRO_FEES)
            .deductions(UPDATED_DEDUCTIONS);
        return ficheDePaie;
    }

    @BeforeEach
    public void initTest() {
        ficheDePaie = createEntity(em);
    }

    @Test
    @Transactional
    void createFicheDePaie() throws Exception {
        int databaseSizeBeforeCreate = ficheDePaieRepository.findAll().size();
        // Create the FicheDePaie
        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isCreated());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeCreate + 1);
        FicheDePaie testFicheDePaie = ficheDePaieList.get(ficheDePaieList.size() - 1);
        assertThat(testFicheDePaie.getSalaireBrut()).isEqualTo(DEFAULT_SALAIRE_BRUT);
        assertThat(testFicheDePaie.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFicheDePaie.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFicheDePaie.getDatepaiement()).isEqualTo(DEFAULT_DATEPAIEMENT);
        assertThat(testFicheDePaie.getSalaireNet()).isEqualTo(DEFAULT_SALAIRE_NET);
        assertThat(testFicheDePaie.getMontantNetAvantImpots()).isEqualTo(DEFAULT_MONTANT_NET_AVANT_IMPOTS);
        assertThat(testFicheDePaie.getProFees()).isEqualTo(DEFAULT_PRO_FEES);
        assertThat(testFicheDePaie.getDeductions()).isEqualTo(DEFAULT_DEDUCTIONS);
    }

    @Test
    @Transactional
    void createFicheDePaieWithExistingId() throws Exception {
        // Create the FicheDePaie with an existing ID
        ficheDePaie.setId(1L);

        int databaseSizeBeforeCreate = ficheDePaieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isBadRequest());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSalaireBrutIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheDePaieRepository.findAll().size();
        // set the field null
        ficheDePaie.setSalaireBrut(null);

        // Create the FicheDePaie, which fails.

        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isBadRequest());

        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheDePaieRepository.findAll().size();
        // set the field null
        ficheDePaie.setStartDate(null);

        // Create the FicheDePaie, which fails.

        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isBadRequest());

        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheDePaieRepository.findAll().size();
        // set the field null
        ficheDePaie.setEndDate(null);

        // Create the FicheDePaie, which fails.

        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isBadRequest());

        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatepaiementIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheDePaieRepository.findAll().size();
        // set the field null
        ficheDePaie.setDatepaiement(null);

        // Create the FicheDePaie, which fails.

        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isBadRequest());

        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalaireNetIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheDePaieRepository.findAll().size();
        // set the field null
        ficheDePaie.setSalaireNet(null);

        // Create the FicheDePaie, which fails.

        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isBadRequest());

        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantNetAvantImpotsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheDePaieRepository.findAll().size();
        // set the field null
        ficheDePaie.setMontantNetAvantImpots(null);

        // Create the FicheDePaie, which fails.

        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isBadRequest());

        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProFeesIsRequired() throws Exception {
        int databaseSizeBeforeTest = ficheDePaieRepository.findAll().size();
        // set the field null
        ficheDePaie.setProFees(null);

        // Create the FicheDePaie, which fails.

        restFicheDePaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isBadRequest());

        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFicheDePaies() throws Exception {
        // Initialize the database
        ficheDePaieRepository.saveAndFlush(ficheDePaie);

        // Get all the ficheDePaieList
        restFicheDePaieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ficheDePaie.getId().intValue())))
            .andExpect(jsonPath("$.[*].salaireBrut").value(hasItem(DEFAULT_SALAIRE_BRUT.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].datepaiement").value(hasItem(DEFAULT_DATEPAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].salaireNet").value(hasItem(DEFAULT_SALAIRE_NET.doubleValue())))
            .andExpect(jsonPath("$.[*].montantNetAvantImpots").value(hasItem(DEFAULT_MONTANT_NET_AVANT_IMPOTS.doubleValue())))
            .andExpect(jsonPath("$.[*].proFees").value(hasItem(DEFAULT_PRO_FEES.doubleValue())))
            .andExpect(jsonPath("$.[*].deductions").value(hasItem(DEFAULT_DEDUCTIONS.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFicheDePaiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(ficheDePaieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFicheDePaieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ficheDePaieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFicheDePaiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ficheDePaieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFicheDePaieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ficheDePaieRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFicheDePaie() throws Exception {
        // Initialize the database
        ficheDePaieRepository.saveAndFlush(ficheDePaie);

        // Get the ficheDePaie
        restFicheDePaieMockMvc
            .perform(get(ENTITY_API_URL_ID, ficheDePaie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ficheDePaie.getId().intValue()))
            .andExpect(jsonPath("$.salaireBrut").value(DEFAULT_SALAIRE_BRUT.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.datepaiement").value(DEFAULT_DATEPAIEMENT.toString()))
            .andExpect(jsonPath("$.salaireNet").value(DEFAULT_SALAIRE_NET.doubleValue()))
            .andExpect(jsonPath("$.montantNetAvantImpots").value(DEFAULT_MONTANT_NET_AVANT_IMPOTS.doubleValue()))
            .andExpect(jsonPath("$.proFees").value(DEFAULT_PRO_FEES.doubleValue()))
            .andExpect(jsonPath("$.deductions").value(DEFAULT_DEDUCTIONS.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFicheDePaie() throws Exception {
        // Get the ficheDePaie
        restFicheDePaieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFicheDePaie() throws Exception {
        // Initialize the database
        ficheDePaieRepository.saveAndFlush(ficheDePaie);

        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();

        // Update the ficheDePaie
        FicheDePaie updatedFicheDePaie = ficheDePaieRepository.findById(ficheDePaie.getId()).get();
        // Disconnect from session so that the updates on updatedFicheDePaie are not directly saved in db
        em.detach(updatedFicheDePaie);
        updatedFicheDePaie
            .salaireBrut(UPDATED_SALAIRE_BRUT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .datepaiement(UPDATED_DATEPAIEMENT)
            .salaireNet(UPDATED_SALAIRE_NET)
            .montantNetAvantImpots(UPDATED_MONTANT_NET_AVANT_IMPOTS)
            .proFees(UPDATED_PRO_FEES)
            .deductions(UPDATED_DEDUCTIONS);

        restFicheDePaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFicheDePaie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFicheDePaie))
            )
            .andExpect(status().isOk());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
        FicheDePaie testFicheDePaie = ficheDePaieList.get(ficheDePaieList.size() - 1);
        assertThat(testFicheDePaie.getSalaireBrut()).isEqualTo(UPDATED_SALAIRE_BRUT);
        assertThat(testFicheDePaie.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFicheDePaie.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFicheDePaie.getDatepaiement()).isEqualTo(UPDATED_DATEPAIEMENT);
        assertThat(testFicheDePaie.getSalaireNet()).isEqualTo(UPDATED_SALAIRE_NET);
        assertThat(testFicheDePaie.getMontantNetAvantImpots()).isEqualTo(UPDATED_MONTANT_NET_AVANT_IMPOTS);
        assertThat(testFicheDePaie.getProFees()).isEqualTo(UPDATED_PRO_FEES);
        assertThat(testFicheDePaie.getDeductions()).isEqualTo(UPDATED_DEDUCTIONS);
    }

    @Test
    @Transactional
    void putNonExistingFicheDePaie() throws Exception {
        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();
        ficheDePaie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFicheDePaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ficheDePaie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheDePaie))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFicheDePaie() throws Exception {
        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();
        ficheDePaie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFicheDePaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ficheDePaie))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFicheDePaie() throws Exception {
        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();
        ficheDePaie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFicheDePaieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ficheDePaie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFicheDePaieWithPatch() throws Exception {
        // Initialize the database
        ficheDePaieRepository.saveAndFlush(ficheDePaie);

        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();

        // Update the ficheDePaie using partial update
        FicheDePaie partialUpdatedFicheDePaie = new FicheDePaie();
        partialUpdatedFicheDePaie.setId(ficheDePaie.getId());

        partialUpdatedFicheDePaie.endDate(UPDATED_END_DATE).datepaiement(UPDATED_DATEPAIEMENT).salaireNet(UPDATED_SALAIRE_NET);

        restFicheDePaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFicheDePaie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFicheDePaie))
            )
            .andExpect(status().isOk());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
        FicheDePaie testFicheDePaie = ficheDePaieList.get(ficheDePaieList.size() - 1);
        assertThat(testFicheDePaie.getSalaireBrut()).isEqualTo(DEFAULT_SALAIRE_BRUT);
        assertThat(testFicheDePaie.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFicheDePaie.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFicheDePaie.getDatepaiement()).isEqualTo(UPDATED_DATEPAIEMENT);
        assertThat(testFicheDePaie.getSalaireNet()).isEqualTo(UPDATED_SALAIRE_NET);
        assertThat(testFicheDePaie.getMontantNetAvantImpots()).isEqualTo(DEFAULT_MONTANT_NET_AVANT_IMPOTS);
        assertThat(testFicheDePaie.getProFees()).isEqualTo(DEFAULT_PRO_FEES);
        assertThat(testFicheDePaie.getDeductions()).isEqualTo(DEFAULT_DEDUCTIONS);
    }

    @Test
    @Transactional
    void fullUpdateFicheDePaieWithPatch() throws Exception {
        // Initialize the database
        ficheDePaieRepository.saveAndFlush(ficheDePaie);

        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();

        // Update the ficheDePaie using partial update
        FicheDePaie partialUpdatedFicheDePaie = new FicheDePaie();
        partialUpdatedFicheDePaie.setId(ficheDePaie.getId());

        partialUpdatedFicheDePaie
            .salaireBrut(UPDATED_SALAIRE_BRUT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .datepaiement(UPDATED_DATEPAIEMENT)
            .salaireNet(UPDATED_SALAIRE_NET)
            .montantNetAvantImpots(UPDATED_MONTANT_NET_AVANT_IMPOTS)
            .proFees(UPDATED_PRO_FEES)
            .deductions(UPDATED_DEDUCTIONS);

        restFicheDePaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFicheDePaie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFicheDePaie))
            )
            .andExpect(status().isOk());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
        FicheDePaie testFicheDePaie = ficheDePaieList.get(ficheDePaieList.size() - 1);
        assertThat(testFicheDePaie.getSalaireBrut()).isEqualTo(UPDATED_SALAIRE_BRUT);
        assertThat(testFicheDePaie.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFicheDePaie.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFicheDePaie.getDatepaiement()).isEqualTo(UPDATED_DATEPAIEMENT);
        assertThat(testFicheDePaie.getSalaireNet()).isEqualTo(UPDATED_SALAIRE_NET);
        assertThat(testFicheDePaie.getMontantNetAvantImpots()).isEqualTo(UPDATED_MONTANT_NET_AVANT_IMPOTS);
        assertThat(testFicheDePaie.getProFees()).isEqualTo(UPDATED_PRO_FEES);
        assertThat(testFicheDePaie.getDeductions()).isEqualTo(UPDATED_DEDUCTIONS);
    }

    @Test
    @Transactional
    void patchNonExistingFicheDePaie() throws Exception {
        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();
        ficheDePaie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFicheDePaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ficheDePaie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ficheDePaie))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFicheDePaie() throws Exception {
        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();
        ficheDePaie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFicheDePaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ficheDePaie))
            )
            .andExpect(status().isBadRequest());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFicheDePaie() throws Exception {
        int databaseSizeBeforeUpdate = ficheDePaieRepository.findAll().size();
        ficheDePaie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFicheDePaieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ficheDePaie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FicheDePaie in the database
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFicheDePaie() throws Exception {
        // Initialize the database
        ficheDePaieRepository.saveAndFlush(ficheDePaie);

        int databaseSizeBeforeDelete = ficheDePaieRepository.findAll().size();

        // Delete the ficheDePaie
        restFicheDePaieMockMvc
            .perform(delete(ENTITY_API_URL_ID, ficheDePaie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FicheDePaie> ficheDePaieList = ficheDePaieRepository.findAll();
        assertThat(ficheDePaieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
