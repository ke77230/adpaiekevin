package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.FicheDePaie;
import fr.adaming.adp.repository.FicheDePaieRepository;
import fr.adaming.adp.service.FicheDePaieService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FicheDePaie}.
 */
@Service
@Transactional
public class FicheDePaieServiceImpl implements FicheDePaieService {

    private final Logger log = LoggerFactory.getLogger(FicheDePaieServiceImpl.class);

    private final FicheDePaieRepository ficheDePaieRepository;

    public FicheDePaieServiceImpl(FicheDePaieRepository ficheDePaieRepository) {
        this.ficheDePaieRepository = ficheDePaieRepository;
    }

    @Override
    public FicheDePaie save(FicheDePaie ficheDePaie) {
        log.debug("Request to save FicheDePaie : {}", ficheDePaie);
        return ficheDePaieRepository.save(ficheDePaie);
    }

    @Override
    public FicheDePaie update(FicheDePaie ficheDePaie) {
        log.debug("Request to update FicheDePaie : {}", ficheDePaie);
        return ficheDePaieRepository.save(ficheDePaie);
    }

    @Override
    public Optional<FicheDePaie> partialUpdate(FicheDePaie ficheDePaie) {
        log.debug("Request to partially update FicheDePaie : {}", ficheDePaie);

        return ficheDePaieRepository
            .findById(ficheDePaie.getId())
            .map(existingFicheDePaie -> {
                if (ficheDePaie.getSalaireBrut() != null) {
                    existingFicheDePaie.setSalaireBrut(ficheDePaie.getSalaireBrut());
                }
                if (ficheDePaie.getStartDate() != null) {
                    existingFicheDePaie.setStartDate(ficheDePaie.getStartDate());
                }
                if (ficheDePaie.getEndDate() != null) {
                    existingFicheDePaie.setEndDate(ficheDePaie.getEndDate());
                }
                if (ficheDePaie.getDatepaiement() != null) {
                    existingFicheDePaie.setDatepaiement(ficheDePaie.getDatepaiement());
                }
                if (ficheDePaie.getSalaireNet() != null) {
                    existingFicheDePaie.setSalaireNet(ficheDePaie.getSalaireNet());
                }
                if (ficheDePaie.getMontantNetAvantImpots() != null) {
                    existingFicheDePaie.setMontantNetAvantImpots(ficheDePaie.getMontantNetAvantImpots());
                }
                if (ficheDePaie.getProFees() != null) {
                    existingFicheDePaie.setProFees(ficheDePaie.getProFees());
                }
                if (ficheDePaie.getDeductions() != null) {
                    existingFicheDePaie.setDeductions(ficheDePaie.getDeductions());
                }

                return existingFicheDePaie;
            })
            .map(ficheDePaieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FicheDePaie> findAll() {
        log.debug("Request to get all FicheDePaies");
        return ficheDePaieRepository.findAll();
    }

    public Page<FicheDePaie> findAllWithEagerRelationships(Pageable pageable) {
        return ficheDePaieRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FicheDePaie> findOne(Long id) {
        log.debug("Request to get FicheDePaie : {}", id);
        return ficheDePaieRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FicheDePaie : {}", id);
        ficheDePaieRepository.deleteById(id);
    }
}
