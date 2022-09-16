package fr.adaming.adp.repository;

import fr.adaming.adp.domain.Conge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Conge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CongeRepository extends JpaRepository<Conge, Long> {}
