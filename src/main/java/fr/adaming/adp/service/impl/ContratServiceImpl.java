package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.Contrat;
import fr.adaming.adp.repository.ContratRepository;
import fr.adaming.adp.service.ContratService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contrat}.
 */
@Service
@Transactional
public class ContratServiceImpl implements ContratService {

    private final Logger log = LoggerFactory.getLogger(ContratServiceImpl.class);

    private final ContratRepository contratRepository;

    public ContratServiceImpl(ContratRepository contratRepository) {
        this.contratRepository = contratRepository;
    }

    @Override
    public Contrat save(Contrat contrat) {
        log.debug("Request to save Contrat : {}", contrat);
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat update(Contrat contrat) {
        log.debug("Request to update Contrat : {}", contrat);
        return contratRepository.save(contrat);
    }

    @Override
    public Optional<Contrat> partialUpdate(Contrat contrat) {
        log.debug("Request to partially update Contrat : {}", contrat);

        return contratRepository
            .findById(contrat.getId())
            .map(existingContrat -> {
                if (contrat.getSalaireBase() != null) {
                    existingContrat.setSalaireBase(contrat.getSalaireBase());
                }
                if (contrat.getEmploi() != null) {
                    existingContrat.setEmploi(contrat.getEmploi());
                }
                if (contrat.getDateArrive() != null) {
                    existingContrat.setDateArrive(contrat.getDateArrive());
                }
                if (contrat.getClassification() != null) {
                    existingContrat.setClassification(contrat.getClassification());
                }
                if (contrat.getTypeForfait() != null) {
                    existingContrat.setTypeForfait(contrat.getTypeForfait());
                }
                if (contrat.getNbHeure() != null) {
                    existingContrat.setNbHeure(contrat.getNbHeure());
                }

                return existingContrat;
            })
            .map(contratRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        log.debug("Request to get all Contrats");
        return contratRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contrat> findOne(Long id) {
        log.debug("Request to get Contrat : {}", id);
        return contratRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contrat : {}", id);
        contratRepository.deleteById(id);
    }
}
