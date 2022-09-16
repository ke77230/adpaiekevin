package fr.adaming.adp.service;

import fr.adaming.adp.domain.ConventionCollective;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ConventionCollective}.
 */
public interface ConventionCollectiveService {
    /**
     * Save a conventionCollective.
     *
     * @param conventionCollective the entity to save.
     * @return the persisted entity.
     */
    ConventionCollective save(ConventionCollective conventionCollective);

    /**
     * Updates a conventionCollective.
     *
     * @param conventionCollective the entity to update.
     * @return the persisted entity.
     */
    ConventionCollective update(ConventionCollective conventionCollective);

    /**
     * Partially updates a conventionCollective.
     *
     * @param conventionCollective the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConventionCollective> partialUpdate(ConventionCollective conventionCollective);

    /**
     * Get all the conventionCollectives.
     *
     * @return the list of entities.
     */
    List<ConventionCollective> findAll();

    /**
     * Get the "id" conventionCollective.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConventionCollective> findOne(Long id);

    /**
     * Delete the "id" conventionCollective.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
