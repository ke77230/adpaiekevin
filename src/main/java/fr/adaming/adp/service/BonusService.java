package fr.adaming.adp.service;

import fr.adaming.adp.domain.Bonus;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Bonus}.
 */
public interface BonusService {
    /**
     * Save a bonus.
     *
     * @param bonus the entity to save.
     * @return the persisted entity.
     */
    Bonus save(Bonus bonus);

    /**
     * Updates a bonus.
     *
     * @param bonus the entity to update.
     * @return the persisted entity.
     */
    Bonus update(Bonus bonus);

    /**
     * Partially updates a bonus.
     *
     * @param bonus the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Bonus> partialUpdate(Bonus bonus);

    /**
     * Get all the bonuses.
     *
     * @return the list of entities.
     */
    List<Bonus> findAll();

    /**
     * Get the "id" bonus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Bonus> findOne(Long id);

    /**
     * Delete the "id" bonus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
