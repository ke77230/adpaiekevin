package fr.adaming.adp.repository;

import fr.adaming.adp.domain.Employeur;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EmployeurRepositoryWithBagRelationshipsImpl implements EmployeurRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Employeur> fetchBagRelationships(Optional<Employeur> employeur) {
        return employeur.map(this::fetchConventionCollectives);
    }

    @Override
    public Page<Employeur> fetchBagRelationships(Page<Employeur> employeurs) {
        return new PageImpl<>(fetchBagRelationships(employeurs.getContent()), employeurs.getPageable(), employeurs.getTotalElements());
    }

    @Override
    public List<Employeur> fetchBagRelationships(List<Employeur> employeurs) {
        return Optional.of(employeurs).map(this::fetchConventionCollectives).orElse(Collections.emptyList());
    }

    Employeur fetchConventionCollectives(Employeur result) {
        return entityManager
            .createQuery(
                "select employeur from Employeur employeur left join fetch employeur.conventionCollectives where employeur is :employeur",
                Employeur.class
            )
            .setParameter("employeur", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Employeur> fetchConventionCollectives(List<Employeur> employeurs) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, employeurs.size()).forEach(index -> order.put(employeurs.get(index).getId(), index));
        List<Employeur> result = entityManager
            .createQuery(
                "select distinct employeur from Employeur employeur left join fetch employeur.conventionCollectives where employeur in :employeurs",
                Employeur.class
            )
            .setParameter("employeurs", employeurs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
