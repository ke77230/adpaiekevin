package fr.adaming.adp.repository;

import fr.adaming.adp.domain.Location;
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
public class LocationRepositoryWithBagRelationshipsImpl implements LocationRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Location> fetchBagRelationships(Optional<Location> location) {
        return location.map(this::fetchEmployees).map(this::fetchEmployeurs);
    }

    @Override
    public Page<Location> fetchBagRelationships(Page<Location> locations) {
        return new PageImpl<>(fetchBagRelationships(locations.getContent()), locations.getPageable(), locations.getTotalElements());
    }

    @Override
    public List<Location> fetchBagRelationships(List<Location> locations) {
        return Optional.of(locations).map(this::fetchEmployees).map(this::fetchEmployeurs).orElse(Collections.emptyList());
    }

    Location fetchEmployees(Location result) {
        return entityManager
            .createQuery(
                "select location from Location location left join fetch location.employees where location is :location",
                Location.class
            )
            .setParameter("location", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Location> fetchEmployees(List<Location> locations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, locations.size()).forEach(index -> order.put(locations.get(index).getId(), index));
        List<Location> result = entityManager
            .createQuery(
                "select distinct location from Location location left join fetch location.employees where location in :locations",
                Location.class
            )
            .setParameter("locations", locations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Location fetchEmployeurs(Location result) {
        return entityManager
            .createQuery(
                "select location from Location location left join fetch location.employeurs where location is :location",
                Location.class
            )
            .setParameter("location", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Location> fetchEmployeurs(List<Location> locations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, locations.size()).forEach(index -> order.put(locations.get(index).getId(), index));
        List<Location> result = entityManager
            .createQuery(
                "select distinct location from Location location left join fetch location.employeurs where location in :locations",
                Location.class
            )
            .setParameter("locations", locations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
