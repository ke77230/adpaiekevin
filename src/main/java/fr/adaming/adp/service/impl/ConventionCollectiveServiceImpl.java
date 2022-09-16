package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.ConventionCollective;
import fr.adaming.adp.repository.ConventionCollectiveRepository;
import fr.adaming.adp.service.ConventionCollectiveService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConventionCollective}.
 */
@Service
@Transactional
public class ConventionCollectiveServiceImpl implements ConventionCollectiveService {

    private final Logger log = LoggerFactory.getLogger(ConventionCollectiveServiceImpl.class);

    private final ConventionCollectiveRepository conventionCollectiveRepository;

    public ConventionCollectiveServiceImpl(ConventionCollectiveRepository conventionCollectiveRepository) {
        this.conventionCollectiveRepository = conventionCollectiveRepository;
    }

    @Override
    public ConventionCollective save(ConventionCollective conventionCollective) {
        log.debug("Request to save ConventionCollective : {}", conventionCollective);
        return conventionCollectiveRepository.save(conventionCollective);
    }

    @Override
    public ConventionCollective update(ConventionCollective conventionCollective) {
        log.debug("Request to update ConventionCollective : {}", conventionCollective);
        return conventionCollectiveRepository.save(conventionCollective);
    }

    @Override
    public Optional<ConventionCollective> partialUpdate(ConventionCollective conventionCollective) {
        log.debug("Request to partially update ConventionCollective : {}", conventionCollective);

        return conventionCollectiveRepository
            .findById(conventionCollective.getId())
            .map(existingConventionCollective -> {
                if (conventionCollective.getIdcc() != null) {
                    existingConventionCollective.setIdcc(conventionCollective.getIdcc());
                }
                if (conventionCollective.getNom() != null) {
                    existingConventionCollective.setNom(conventionCollective.getNom());
                }
                if (conventionCollective.getPosition() != null) {
                    existingConventionCollective.setPosition(conventionCollective.getPosition());
                }
                if (conventionCollective.getCoefficient() != null) {
                    existingConventionCollective.setCoefficient(conventionCollective.getCoefficient());
                }
                if (conventionCollective.getValeurPoint() != null) {
                    existingConventionCollective.setValeurPoint(conventionCollective.getValeurPoint());
                }
                if (conventionCollective.getBaseFixe() != null) {
                    existingConventionCollective.setBaseFixe(conventionCollective.getBaseFixe());
                }
                if (conventionCollective.getSalaireMinimaux() != null) {
                    existingConventionCollective.setSalaireMinimaux(conventionCollective.getSalaireMinimaux());
                }

                return existingConventionCollective;
            })
            .map(conventionCollectiveRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConventionCollective> findAll() {
        log.debug("Request to get all ConventionCollectives");
        return conventionCollectiveRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConventionCollective> findOne(Long id) {
        log.debug("Request to get ConventionCollective : {}", id);
        return conventionCollectiveRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConventionCollective : {}", id);
        conventionCollectiveRepository.deleteById(id);
    }
}
