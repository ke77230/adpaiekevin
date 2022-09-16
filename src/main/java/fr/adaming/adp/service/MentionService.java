package fr.adaming.adp.service;

import fr.adaming.adp.domain.Mention;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Mention}.
 */
public interface MentionService {
    /**
     * Save a mention.
     *
     * @param mention the entity to save.
     * @return the persisted entity.
     */
    Mention save(Mention mention);

    /**
     * Updates a mention.
     *
     * @param mention the entity to update.
     * @return the persisted entity.
     */
    Mention update(Mention mention);

    /**
     * Partially updates a mention.
     *
     * @param mention the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Mention> partialUpdate(Mention mention);

    /**
     * Get all the mentions.
     *
     * @return the list of entities.
     */
    List<Mention> findAll();

    /**
     * Get the "id" mention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mention> findOne(Long id);

    /**
     * Delete the "id" mention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
