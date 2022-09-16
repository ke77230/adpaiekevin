package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.Cotisation;
import fr.adaming.adp.repository.CotisationRepository;
import fr.adaming.adp.service.CotisationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cotisation}.
 */
@Service
@Transactional
public class CotisationServiceImpl implements CotisationService {

    private final Logger log = LoggerFactory.getLogger(CotisationServiceImpl.class);

    private final CotisationRepository cotisationRepository;

    public CotisationServiceImpl(CotisationRepository cotisationRepository) {
        this.cotisationRepository = cotisationRepository;
    }

    @Override
    public Cotisation save(Cotisation cotisation) {
        log.debug("Request to save Cotisation : {}", cotisation);
        return cotisationRepository.save(cotisation);
    }

    @Override
    public Cotisation update(Cotisation cotisation) {
        log.debug("Request to update Cotisation : {}", cotisation);
        return cotisationRepository.save(cotisation);
    }

    @Override
    public Optional<Cotisation> partialUpdate(Cotisation cotisation) {
        log.debug("Request to partially update Cotisation : {}", cotisation);

        return cotisationRepository
            .findById(cotisation.getId())
            .map(existingCotisation -> {
                if (cotisation.getName() != null) {
                    existingCotisation.setName(cotisation.getName());
                }
                if (cotisation.getFamille() != null) {
                    existingCotisation.setFamille(cotisation.getFamille());
                }
                if (cotisation.getTaux() != null) {
                    existingCotisation.setTaux(cotisation.getTaux());
                }
                if (cotisation.getStartDate() != null) {
                    existingCotisation.setStartDate(cotisation.getStartDate());
                }
                if (cotisation.getEndDate() != null) {
                    existingCotisation.setEndDate(cotisation.getEndDate());
                }
                if (cotisation.getActuel() != null) {
                    existingCotisation.setActuel(cotisation.getActuel());
                }

                return existingCotisation;
            })
            .map(cotisationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cotisation> findAll() {
        log.debug("Request to get all Cotisations");
        return cotisationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cotisation> findOne(Long id) {
        log.debug("Request to get Cotisation : {}", id);
        return cotisationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cotisation : {}", id);
        cotisationRepository.deleteById(id);
    }
}
