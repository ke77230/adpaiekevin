package fr.adaming.adp.service;

import fr.adaming.adp.domain.Cotisation;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Cotisation}.
 */
public interface CotisationService {
    /**
     * Save a cotisation.
     *
     * @param cotisation the entity to save.
     * @return the persisted entity.
     */
    Cotisation save(Cotisation cotisation);

    /**
     * Updates a cotisation.
     *
     * @param cotisation the entity to update.
     * @return the persisted entity.
     */
    Cotisation update(Cotisation cotisation);

    /**
     * Partially updates a cotisation.
     *
     * @param cotisation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cotisation> partialUpdate(Cotisation cotisation);

    /**
     * Get all the cotisations.
     *
     * @return the list of entities.
     */
    List<Cotisation> findAll();

    /**
     * Get the "id" cotisation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cotisation> findOne(Long id);

    /**
     * Delete the "id" cotisation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
