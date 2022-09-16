package fr.adaming.adp.service;

import fr.adaming.adp.domain.TauxDImposition;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link TauxDImposition}.
 */
public interface TauxDImpositionService {
    /**
     * Save a tauxDImposition.
     *
     * @param tauxDImposition the entity to save.
     * @return the persisted entity.
     */
    TauxDImposition save(TauxDImposition tauxDImposition);

    /**
     * Updates a tauxDImposition.
     *
     * @param tauxDImposition the entity to update.
     * @return the persisted entity.
     */
    TauxDImposition update(TauxDImposition tauxDImposition);

    /**
     * Partially updates a tauxDImposition.
     *
     * @param tauxDImposition the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TauxDImposition> partialUpdate(TauxDImposition tauxDImposition);

    /**
     * Get all the tauxDImpositions.
     *
     * @return the list of entities.
     */
    List<TauxDImposition> findAll();

    /**
     * Get the "id" tauxDImposition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TauxDImposition> findOne(Long id);

    /**
     * Delete the "id" tauxDImposition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
