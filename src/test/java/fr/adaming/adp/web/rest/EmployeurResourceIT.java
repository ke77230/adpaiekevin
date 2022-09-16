package fr.adaming.adp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.adaming.adp.IntegrationTest;
import fr.adaming.adp.domain.Employeur;
import fr.adaming.adp.repository.EmployeurRepository;
import fr.adaming.adp.service.EmployeurService;
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
 * Integration tests for the {@link EmployeurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeurResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_SIRET = 14;
    private static final Integer UPDATED_NUMERO_SIRET = 15;

    private static final Integer DEFAULT_NUM_APE = 5;
    private static final Integer UPDATED_NUM_APE = 6;

    private static final Integer DEFAULT_NUM_URSSAF = 14;
    private static final Integer UPDATED_NUM_URSSAF = 15;

    private static final String ENTITY_API_URL = "/api/employeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeurRepository employeurRepository;

    @Mock
    private EmployeurRepository employeurRepositoryMock;

    @Mock
    private EmployeurService employeurServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeurMockMvc;

    private Employeur employeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employeur createEntity(EntityManager em) {
        Employeur employeur = new Employeur()
            .name(DEFAULT_NAME)
            .numeroSiret(DEFAULT_NUMERO_SIRET)
            .numApe(DEFAULT_NUM_APE)
            .numUrssaf(DEFAULT_NUM_URSSAF);
        return employeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employeur createUpdatedEntity(EntityManager em) {
        Employeur employeur = new Employeur()
            .name(UPDATED_NAME)
            .numeroSiret(UPDATED_NUMERO_SIRET)
            .numApe(UPDATED_NUM_APE)
            .numUrssaf(UPDATED_NUM_URSSAF);
        return employeur;
    }

    @BeforeEach
    public void initTest() {
        employeur = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeur() throws Exception {
        int databaseSizeBeforeCreate = employeurRepository.findAll().size();
        // Create the Employeur
        restEmployeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeur)))
            .andExpect(status().isCreated());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeCreate + 1);
        Employeur testEmployeur = employeurList.get(employeurList.size() - 1);
        assertThat(testEmployeur.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployeur.getNumeroSiret()).isEqualTo(DEFAULT_NUMERO_SIRET);
        assertThat(testEmployeur.getNumApe()).isEqualTo(DEFAULT_NUM_APE);
        assertThat(testEmployeur.getNumUrssaf()).isEqualTo(DEFAULT_NUM_URSSAF);
    }

    @Test
    @Transactional
    void createEmployeurWithExistingId() throws Exception {
        // Create the Employeur with an existing ID
        employeur.setId(1L);

        int databaseSizeBeforeCreate = employeurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeur)))
            .andExpect(status().isBadRequest());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeurRepository.findAll().size();
        // set the field null
        employeur.setName(null);

        // Create the Employeur, which fails.

        restEmployeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeur)))
            .andExpect(status().isBadRequest());

        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroSiretIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeurRepository.findAll().size();
        // set the field null
        employeur.setNumeroSiret(null);

        // Create the Employeur, which fails.

        restEmployeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeur)))
            .andExpect(status().isBadRequest());

        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumApeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeurRepository.findAll().size();
        // set the field null
        employeur.setNumApe(null);

        // Create the Employeur, which fails.

        restEmployeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeur)))
            .andExpect(status().isBadRequest());

        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumUrssafIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeurRepository.findAll().size();
        // set the field null
        employeur.setNumUrssaf(null);

        // Create the Employeur, which fails.

        restEmployeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeur)))
            .andExpect(status().isBadRequest());

        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeurs() throws Exception {
        // Initialize the database
        employeurRepository.saveAndFlush(employeur);

        // Get all the employeurList
        restEmployeurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].numeroSiret").value(hasItem(DEFAULT_NUMERO_SIRET)))
            .andExpect(jsonPath("$.[*].numApe").value(hasItem(DEFAULT_NUM_APE)))
            .andExpect(jsonPath("$.[*].numUrssaf").value(hasItem(DEFAULT_NUM_URSSAF)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeursWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeurRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployeur() throws Exception {
        // Initialize the database
        employeurRepository.saveAndFlush(employeur);

        // Get the employeur
        restEmployeurMockMvc
            .perform(get(ENTITY_API_URL_ID, employeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeur.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.numeroSiret").value(DEFAULT_NUMERO_SIRET))
            .andExpect(jsonPath("$.numApe").value(DEFAULT_NUM_APE))
            .andExpect(jsonPath("$.numUrssaf").value(DEFAULT_NUM_URSSAF));
    }

    @Test
    @Transactional
    void getNonExistingEmployeur() throws Exception {
        // Get the employeur
        restEmployeurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeur() throws Exception {
        // Initialize the database
        employeurRepository.saveAndFlush(employeur);

        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();

        // Update the employeur
        Employeur updatedEmployeur = employeurRepository.findById(employeur.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeur are not directly saved in db
        em.detach(updatedEmployeur);
        updatedEmployeur.name(UPDATED_NAME).numeroSiret(UPDATED_NUMERO_SIRET).numApe(UPDATED_NUM_APE).numUrssaf(UPDATED_NUM_URSSAF);

        restEmployeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployeur))
            )
            .andExpect(status().isOk());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
        Employeur testEmployeur = employeurList.get(employeurList.size() - 1);
        assertThat(testEmployeur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployeur.getNumeroSiret()).isEqualTo(UPDATED_NUMERO_SIRET);
        assertThat(testEmployeur.getNumApe()).isEqualTo(UPDATED_NUM_APE);
        assertThat(testEmployeur.getNumUrssaf()).isEqualTo(UPDATED_NUM_URSSAF);
    }

    @Test
    @Transactional
    void putNonExistingEmployeur() throws Exception {
        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();
        employeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeur() throws Exception {
        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();
        employeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeur() throws Exception {
        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();
        employeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeurWithPatch() throws Exception {
        // Initialize the database
        employeurRepository.saveAndFlush(employeur);

        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();

        // Update the employeur using partial update
        Employeur partialUpdatedEmployeur = new Employeur();
        partialUpdatedEmployeur.setId(employeur.getId());

        partialUpdatedEmployeur.name(UPDATED_NAME).numeroSiret(UPDATED_NUMERO_SIRET).numApe(UPDATED_NUM_APE);

        restEmployeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeur))
            )
            .andExpect(status().isOk());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
        Employeur testEmployeur = employeurList.get(employeurList.size() - 1);
        assertThat(testEmployeur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployeur.getNumeroSiret()).isEqualTo(UPDATED_NUMERO_SIRET);
        assertThat(testEmployeur.getNumApe()).isEqualTo(UPDATED_NUM_APE);
        assertThat(testEmployeur.getNumUrssaf()).isEqualTo(DEFAULT_NUM_URSSAF);
    }

    @Test
    @Transactional
    void fullUpdateEmployeurWithPatch() throws Exception {
        // Initialize the database
        employeurRepository.saveAndFlush(employeur);

        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();

        // Update the employeur using partial update
        Employeur partialUpdatedEmployeur = new Employeur();
        partialUpdatedEmployeur.setId(employeur.getId());

        partialUpdatedEmployeur.name(UPDATED_NAME).numeroSiret(UPDATED_NUMERO_SIRET).numApe(UPDATED_NUM_APE).numUrssaf(UPDATED_NUM_URSSAF);

        restEmployeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeur))
            )
            .andExpect(status().isOk());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
        Employeur testEmployeur = employeurList.get(employeurList.size() - 1);
        assertThat(testEmployeur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployeur.getNumeroSiret()).isEqualTo(UPDATED_NUMERO_SIRET);
        assertThat(testEmployeur.getNumApe()).isEqualTo(UPDATED_NUM_APE);
        assertThat(testEmployeur.getNumUrssaf()).isEqualTo(UPDATED_NUM_URSSAF);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeur() throws Exception {
        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();
        employeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeur() throws Exception {
        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();
        employeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeur() throws Exception {
        int databaseSizeBeforeUpdate = employeurRepository.findAll().size();
        employeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employeur in the database
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeur() throws Exception {
        // Initialize the database
        employeurRepository.saveAndFlush(employeur);

        int databaseSizeBeforeDelete = employeurRepository.findAll().size();

        // Delete the employeur
        restEmployeurMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employeur> employeurList = employeurRepository.findAll();
        assertThat(employeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
