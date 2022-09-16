package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.Conge;
import fr.adaming.adp.repository.CongeRepository;
import fr.adaming.adp.service.CongeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conge}.
 */
@Service
@Transactional
public class CongeServiceImpl implements CongeService {

    private final Logger log = LoggerFactory.getLogger(CongeServiceImpl.class);

    private final CongeRepository congeRepository;

    public CongeServiceImpl(CongeRepository congeRepository) {
        this.congeRepository = congeRepository;
    }

    @Override
    public Conge save(Conge conge) {
        log.debug("Request to save Conge : {}", conge);
        return congeRepository.save(conge);
    }

    @Override
    public Conge update(Conge conge) {
        log.debug("Request to update Conge : {}", conge);
        return congeRepository.save(conge);
    }

    @Override
    public Optional<Conge> partialUpdate(Conge conge) {
        log.debug("Request to partially update Conge : {}", conge);

        return congeRepository
            .findById(conge.getId())
            .map(existingConge -> {
                if (conge.getHoldateStart() != null) {
                    existingConge.setHoldateStart(conge.getHoldateStart());
                }
                if (conge.getHoldateEnd() != null) {
                    existingConge.setHoldateEnd(conge.getHoldateEnd());
                }
                if (conge.getHoldatePay() != null) {
                    existingConge.setHoldatePay(conge.getHoldatePay());
                }
                if (conge.getNbCongeAcquis() != null) {
                    existingConge.setNbCongeAcquis(conge.getNbCongeAcquis());
                }
                if (conge.getNbCongePris() != null) {
                    existingConge.setNbCongePris(conge.getNbCongePris());
                }
                if (conge.getDateDemande() != null) {
                    existingConge.setDateDemande(conge.getDateDemande());
                }
                if (conge.getDecision() != null) {
                    existingConge.setDecision(conge.getDecision());
                }
                if (conge.getDateReponse() != null) {
                    existingConge.setDateReponse(conge.getDateReponse());
                }

                return existingConge;
            })
            .map(congeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conge> findAll() {
        log.debug("Request to get all Conges");
        return congeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Conge> findOne(Long id) {
        log.debug("Request to get Conge : {}", id);
        return congeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Conge : {}", id);
        congeRepository.deleteById(id);
    }
}
