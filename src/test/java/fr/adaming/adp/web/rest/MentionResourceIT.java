package fr.adaming.adp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.adaming.adp.IntegrationTest;
import fr.adaming.adp.domain.Mention;
import fr.adaming.adp.repository.MentionRepository;
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
 * Integration tests for the {@link MentionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MentionResourceIT {

    private static final String DEFAULT_MENTION = "AAAAAAAAAA";
    private static final String UPDATED_MENTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MentionRepository mentionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMentionMockMvc;

    private Mention mention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mention createEntity(EntityManager em) {
        Mention mention = new Mention().mention(DEFAULT_MENTION);
        return mention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mention createUpdatedEntity(EntityManager em) {
        Mention mention = new Mention().mention(UPDATED_MENTION);
        return mention;
    }

    @BeforeEach
    public void initTest() {
        mention = createEntity(em);
    }

    @Test
    @Transactional
    void createMention() throws Exception {
        int databaseSizeBeforeCreate = mentionRepository.findAll().size();
        // Create the Mention
        restMentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mention)))
            .andExpect(status().isCreated());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeCreate + 1);
        Mention testMention = mentionList.get(mentionList.size() - 1);
        assertThat(testMention.getMention()).isEqualTo(DEFAULT_MENTION);
    }

    @Test
    @Transactional
    void createMentionWithExistingId() throws Exception {
        // Create the Mention with an existing ID
        mention.setId(1L);

        int databaseSizeBeforeCreate = mentionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mention)))
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMentionIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentionRepository.findAll().size();
        // set the field null
        mention.setMention(null);

        // Create the Mention, which fails.

        restMentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mention)))
            .andExpect(status().isBadRequest());

        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMentions() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        // Get all the mentionList
        restMentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mention.getId().intValue())))
            .andExpect(jsonPath("$.[*].mention").value(hasItem(DEFAULT_MENTION)));
    }

    @Test
    @Transactional
    void getMention() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        // Get the mention
        restMentionMockMvc
            .perform(get(ENTITY_API_URL_ID, mention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mention.getId().intValue()))
            .andExpect(jsonPath("$.mention").value(DEFAULT_MENTION));
    }

    @Test
    @Transactional
    void getNonExistingMention() throws Exception {
        // Get the mention
        restMentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMention() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();

        // Update the mention
        Mention updatedMention = mentionRepository.findById(mention.getId()).get();
        // Disconnect from session so that the updates on updatedMention are not directly saved in db
        em.detach(updatedMention);
        updatedMention.mention(UPDATED_MENTION);

        restMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMention))
            )
            .andExpect(status().isOk());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        Mention testMention = mentionList.get(mentionList.size() - 1);
        assertThat(testMention.getMention()).isEqualTo(UPDATED_MENTION);
    }

    @Test
    @Transactional
    void putNonExistingMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        mention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        mention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        mention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMentionWithPatch() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();

        // Update the mention using partial update
        Mention partialUpdatedMention = new Mention();
        partialUpdatedMention.setId(mention.getId());

        partialUpdatedMention.mention(UPDATED_MENTION);

        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMention))
            )
            .andExpect(status().isOk());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        Mention testMention = mentionList.get(mentionList.size() - 1);
        assertThat(testMention.getMention()).isEqualTo(UPDATED_MENTION);
    }

    @Test
    @Transactional
    void fullUpdateMentionWithPatch() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();

        // Update the mention using partial update
        Mention partialUpdatedMention = new Mention();
        partialUpdatedMention.setId(mention.getId());

        partialUpdatedMention.mention(UPDATED_MENTION);

        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMention))
            )
            .andExpect(status().isOk());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        Mention testMention = mentionList.get(mentionList.size() - 1);
        assertThat(testMention.getMention()).isEqualTo(UPDATED_MENTION);
    }

    @Test
    @Transactional
    void patchNonExistingMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        mention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        mention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        mention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMention() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        int databaseSizeBeforeDelete = mentionRepository.findAll().size();

        // Delete the mention
        restMentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, mention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
