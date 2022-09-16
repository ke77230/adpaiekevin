package fr.adaming.adp.repository;

import fr.adaming.adp.domain.ConventionCollective;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConventionCollective entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConventionCollectiveRepository extends JpaRepository<ConventionCollective, Long> {}
