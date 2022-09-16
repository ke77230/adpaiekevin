package fr.adaming.adp.repository;

import fr.adaming.adp.domain.TauxDImposition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TauxDImposition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TauxDImpositionRepository extends JpaRepository<TauxDImposition, Long> {}
