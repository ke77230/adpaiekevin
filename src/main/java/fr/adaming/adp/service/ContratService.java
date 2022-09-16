package fr.adaming.adp.service;

import fr.adaming.adp.domain.Contrat;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Contrat}.
 */
public interface ContratService {
    /**
     * Save a contrat.
     *
     * @param contrat the entity to save.
     * @return the persisted entity.
     */
    Contrat save(Contrat contrat);

    /**
     * Updates a contrat.
     *
     * @param contrat the entity to update.
     * @return the persisted entity.
     */
    Contrat update(Contrat contrat);

    /**
     * Partially updates a contrat.
     *
     * @param contrat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Contrat> partialUpdate(Contrat contrat);

    /**
     * Get all the contrats.
     *
     * @return the list of entities.
     */
    List<Contrat> findAll();

    /**
     * Get the "id" contrat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Contrat> findOne(Long id);

    /**
     * Delete the "id" contrat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
