package fr.adaming.adp.service;

import fr.adaming.adp.domain.FicheDePaie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FicheDePaie}.
 */
public interface FicheDePaieService {
    /**
     * Save a ficheDePaie.
     *
     * @param ficheDePaie the entity to save.
     * @return the persisted entity.
     */
    FicheDePaie save(FicheDePaie ficheDePaie);

    /**
     * Updates a ficheDePaie.
     *
     * @param ficheDePaie the entity to update.
     * @return the persisted entity.
     */
    FicheDePaie update(FicheDePaie ficheDePaie);

    /**
     * Partially updates a ficheDePaie.
     *
     * @param ficheDePaie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FicheDePaie> partialUpdate(FicheDePaie ficheDePaie);

    /**
     * Get all the ficheDePaies.
     *
     * @return the list of entities.
     */
    List<FicheDePaie> findAll();

    /**
     * Get all the ficheDePaies with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FicheDePaie> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" ficheDePaie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FicheDePaie> findOne(Long id);

    /**
     * Delete the "id" ficheDePaie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
