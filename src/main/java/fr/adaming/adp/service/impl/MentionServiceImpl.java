package fr.adaming.adp.service.impl;

import fr.adaming.adp.domain.Mention;
import fr.adaming.adp.repository.MentionRepository;
import fr.adaming.adp.service.MentionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mention}.
 */
@Service
@Transactional
public class MentionServiceImpl implements MentionService {

    private final Logger log = LoggerFactory.getLogger(MentionServiceImpl.class);

    private final MentionRepository mentionRepository;

    public MentionServiceImpl(MentionRepository mentionRepository) {
        this.mentionRepository = mentionRepository;
    }

    @Override
    public Mention save(Mention mention) {
        log.debug("Request to save Mention : {}", mention);
        return mentionRepository.save(mention);
    }

    @Override
    public Mention update(Mention mention) {
        log.debug("Request to update Mention : {}", mention);
        return mentionRepository.save(mention);
    }

    @Override
    public Optional<Mention> partialUpdate(Mention mention) {
        log.debug("Request to partially update Mention : {}", mention);

        return mentionRepository
            .findById(mention.getId())
            .map(existingMention -> {
                if (mention.getMention() != null) {
                    existingMention.setMention(mention.getMention());
                }

                return existingMention;
            })
            .map(mentionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mention> findAll() {
        log.debug("Request to get all Mentions");
        return mentionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mention> findOne(Long id) {
        log.debug("Request to get Mention : {}", id);
        return mentionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mention : {}", id);
        mentionRepository.deleteById(id);
    }
}
