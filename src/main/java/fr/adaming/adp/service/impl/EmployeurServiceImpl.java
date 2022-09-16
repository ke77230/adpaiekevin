package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.Employeur;
import fr.adaming.adp.repository.EmployeurRepository;
import fr.adaming.adp.service.EmployeurService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employeur}.
 */
@Service
@Transactional
public class EmployeurServiceImpl implements EmployeurService {

    private final Logger log = LoggerFactory.getLogger(EmployeurServiceImpl.class);

    private final EmployeurRepository employeurRepository;

    public EmployeurServiceImpl(EmployeurRepository employeurRepository) {
        this.employeurRepository = employeurRepository;
    }

    @Override
    public Employeur save(Employeur employeur) {
        log.debug("Request to save Employeur : {}", employeur);
        return employeurRepository.save(employeur);
    }

    @Override
    public Employeur update(Employeur employeur) {
        log.debug("Request to update Employeur : {}", employeur);
        return employeurRepository.save(employeur);
    }

    @Override
    public Optional<Employeur> partialUpdate(Employeur employeur) {
        log.debug("Request to partially update Employeur : {}", employeur);

        return employeurRepository
            .findById(employeur.getId())
            .map(existingEmployeur -> {
                if (employeur.getName() != null) {
                    existingEmployeur.setName(employeur.getName());
                }
                if (employeur.getNumeroSiret() != null) {
                    existingEmployeur.setNumeroSiret(employeur.getNumeroSiret());
                }
                if (employeur.getNumApe() != null) {
                    existingEmployeur.setNumApe(employeur.getNumApe());
                }
                if (employeur.getNumUrssaf() != null) {
                    existingEmployeur.setNumUrssaf(employeur.getNumUrssaf());
                }

                return existingEmployeur;
            })
            .map(employeurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employeur> findAll() {
        log.debug("Request to get all Employeurs");
        return employeurRepository.findAll();
    }

    public Page<Employeur> findAllWithEagerRelationships(Pageable pageable) {
        return employeurRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employeur> findOne(Long id) {
        log.debug("Request to get Employeur : {}", id);
        return employeurRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employeur : {}", id);
        employeurRepository.deleteById(id);
    }
}
