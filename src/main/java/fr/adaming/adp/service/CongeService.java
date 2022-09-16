package fr.adaming.adp.service;

import fr.adaming.adp.domain.Conge;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Conge}.
 */
public interface CongeService {
    /**
     * Save a conge.
     *
     * @param conge the entity to save.
     * @return the persisted entity.
     */
    Conge save(Conge conge);

    /**
     * Updates a conge.
     *
     * @param conge the entity to update.
     * @return the persisted entity.
     */
    Conge update(Conge conge);

    /**
     * Partially updates a conge.
     *
     * @param conge the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Conge> partialUpdate(Conge conge);

    /**
     * Get all the conges.
     *
     * @return the list of entities.
     */
    List<Conge> findAll();

    /**
     * Get the "id" conge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Conge> findOne(Long id);

    /**
     * Delete the "id" conge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
