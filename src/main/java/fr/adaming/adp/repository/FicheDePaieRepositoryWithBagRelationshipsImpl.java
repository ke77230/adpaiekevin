package fr.adaming.adp.repository;

import fr.adaming.adp.domain.FicheDePaie;
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
public class FicheDePaieRepositoryWithBagRelationshipsImpl implements FicheDePaieRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FicheDePaie> fetchBagRelationships(Optional<FicheDePaie> ficheDePaie) {
        return ficheDePaie.map(this::fetchCotisations).map(this::fetchMentions);
    }

    @Override
    public Page<FicheDePaie> fetchBagRelationships(Page<FicheDePaie> ficheDePaies) {
        return new PageImpl<>(
            fetchBagRelationships(ficheDePaies.getContent()),
            ficheDePaies.getPageable(),
            ficheDePaies.getTotalElements()
        );
    }

    @Override
    public List<FicheDePaie> fetchBagRelationships(List<FicheDePaie> ficheDePaies) {
        return Optional.of(ficheDePaies).map(this::fetchCotisations).map(this::fetchMentions).orElse(Collections.emptyList());
    }

    FicheDePaie fetchCotisations(FicheDePaie result) {
        return entityManager
            .createQuery(
                "select ficheDePaie from FicheDePaie ficheDePaie left join fetch ficheDePaie.cotisations where ficheDePaie is :ficheDePaie",
                FicheDePaie.class
            )
            .setParameter("ficheDePaie", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FicheDePaie> fetchCotisations(List<FicheDePaie> ficheDePaies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ficheDePaies.size()).forEach(index -> order.put(ficheDePaies.get(index).getId(), index));
        List<FicheDePaie> result = entityManager
            .createQuery(
                "select distinct ficheDePaie from FicheDePaie ficheDePaie left join fetch ficheDePaie.cotisations where ficheDePaie in :ficheDePaies",
                FicheDePaie.class
            )
            .setParameter("ficheDePaies", ficheDePaies)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    FicheDePaie fetchMentions(FicheDePaie result) {
        return entityManager
            .createQuery(
                "select ficheDePaie from FicheDePaie ficheDePaie left join fetch ficheDePaie.mentions where ficheDePaie is :ficheDePaie",
                FicheDePaie.class
            )
            .setParameter("ficheDePaie", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FicheDePaie> fetchMentions(List<FicheDePaie> ficheDePaies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ficheDePaies.size()).forEach(index -> order.put(ficheDePaies.get(index).getId(), index));
        List<FicheDePaie> result = entityManager
            .createQuery(
                "select distinct ficheDePaie from FicheDePaie ficheDePaie left join fetch ficheDePaie.mentions where ficheDePaie in :ficheDePaies",
                FicheDePaie.class
            )
            .setParameter("ficheDePaies", ficheDePaies)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
