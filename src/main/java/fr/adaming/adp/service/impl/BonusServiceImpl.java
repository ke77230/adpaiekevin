package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.Bonus;
import fr.adaming.adp.repository.BonusRepository;
import fr.adaming.adp.service.BonusService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bonus}.
 */
@Service
@Transactional
public class BonusServiceImpl implements BonusService {

    private final Logger log = LoggerFactory.getLogger(BonusServiceImpl.class);

    private final BonusRepository bonusRepository;

    public BonusServiceImpl(BonusRepository bonusRepository) {
        this.bonusRepository = bonusRepository;
    }

    @Override
    public Bonus save(Bonus bonus) {
        log.debug("Request to save Bonus : {}", bonus);
        return bonusRepository.save(bonus);
    }

    @Override
    public Bonus update(Bonus bonus) {
        log.debug("Request to update Bonus : {}", bonus);
        return bonusRepository.save(bonus);
    }

    @Override
    public Optional<Bonus> partialUpdate(Bonus bonus) {
        log.debug("Request to partially update Bonus : {}", bonus);

        return bonusRepository
            .findById(bonus.getId())
            .map(existingBonus -> {
                if (bonus.getNom() != null) {
                    existingBonus.setNom(bonus.getNom());
                }
                if (bonus.getMontant() != null) {
                    existingBonus.setMontant(bonus.getMontant());
                }

                return existingBonus;
            })
            .map(bonusRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bonus> findAll() {
        log.debug("Request to get all Bonuses");
        return bonusRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bonus> findOne(Long id) {
        log.debug("Request to get Bonus : {}", id);
        return bonusRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bonus : {}", id);
        bonusRepository.deleteById(id);
    }
}
