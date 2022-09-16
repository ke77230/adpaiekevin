package fr.adaming.adp.service;

import fr.adaming.adp.domain.Employeur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Employeur}.
 */
public interface EmployeurService {
    /**
     * Save a employeur.
     *
     * @param employeur the entity to save.
     * @return the persisted entity.
     */
    Employeur save(Employeur employeur);

    /**
     * Updates a employeur.
     *
     * @param employeur the entity to update.
     * @return the persisted entity.
     */
    Employeur update(Employeur employeur);

    /**
     * Partially updates a employeur.
     *
     * @param employeur the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Employeur> partialUpdate(Employeur employeur);

    /**
     * Get all the employeurs.
     *
     * @return the list of entities.
     */
    List<Employeur> findAll();

    /**
     * Get all the employeurs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Employeur> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" employeur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Employeur> findOne(Long id);

    /**
     * Delete the "id" employeur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
