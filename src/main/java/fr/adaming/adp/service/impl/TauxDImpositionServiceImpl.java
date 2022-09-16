package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.TauxDImposition;
import fr.adaming.adp.repository.TauxDImpositionRepository;
import fr.adaming.adp.service.TauxDImpositionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TauxDImposition}.
 */
@Service
@Transactional
public class TauxDImpositionServiceImpl implements TauxDImpositionService {

    private final Logger log = LoggerFactory.getLogger(TauxDImpositionServiceImpl.class);

    private final TauxDImpositionRepository tauxDImpositionRepository;

    public TauxDImpositionServiceImpl(TauxDImpositionRepository tauxDImpositionRepository) {
        this.tauxDImpositionRepository = tauxDImpositionRepository;
    }

    @Override
    public TauxDImposition save(TauxDImposition tauxDImposition) {
        log.debug("Request to save TauxDImposition : {}", tauxDImposition);
        return tauxDImpositionRepository.save(tauxDImposition);
    }

    @Override
    public TauxDImposition update(TauxDImposition tauxDImposition) {
        log.debug("Request to update TauxDImposition : {}", tauxDImposition);
        return tauxDImpositionRepository.save(tauxDImposition);
    }

    @Override
    public Optional<TauxDImposition> partialUpdate(TauxDImposition tauxDImposition) {
        log.debug("Request to partially update TauxDImposition : {}", tauxDImposition);

        return tauxDImpositionRepository
            .findById(tauxDImposition.getId())
            .map(existingTauxDImposition -> {
                if (tauxDImposition.getTaux() != null) {
                    existingTauxDImposition.setTaux(tauxDImposition.getTaux());
                }
                if (tauxDImposition.getMinSalary() != null) {
                    existingTauxDImposition.setMinSalary(tauxDImposition.getMinSalary());
                }
                if (tauxDImposition.getMaxSalary() != null) {
                    existingTauxDImposition.setMaxSalary(tauxDImposition.getMaxSalary());
                }
                if (tauxDImposition.getStartDate() != null) {
                    existingTauxDImposition.setStartDate(tauxDImposition.getStartDate());
                }
                if (tauxDImposition.getEndDate() != null) {
                    existingTauxDImposition.setEndDate(tauxDImposition.getEndDate());
                }

                return existingTauxDImposition;
            })
            .map(tauxDImpositionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TauxDImposition> findAll() {
        log.debug("Request to get all TauxDImpositions");
        return tauxDImpositionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TauxDImposition> findOne(Long id) {
        log.debug("Request to get TauxDImposition : {}", id);
        return tauxDImpositionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TauxDImposition : {}", id);
        tauxDImpositionRepository.deleteById(id);
    }
}
