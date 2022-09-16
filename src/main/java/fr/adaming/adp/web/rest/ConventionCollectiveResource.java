package fr.adaming.adp.web.rest;

import fr.adaming.adp.domain.ConventionCollective;
import fr.adaming.adp.repository.ConventionCollectiveRepository;
import fr.adaming.adp.service.ConventionCollectiveService;
import fr.adaming.adp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.adaming.adp.domain.ConventionCollective}.
 */
@RestController
@RequestMapping("/api")
public class ConventionCollectiveResource {

    private final Logger log = LoggerFactory.getLogger(ConventionCollectiveResource.class);

    private static final String ENTITY_NAME = "conventionCollective";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConventionCollectiveService conventionCollectiveService;

    private final ConventionCollectiveRepository conventionCollectiveRepository;

    public ConventionCollectiveResource(
        ConventionCollectiveService conventionCollectiveService,
        ConventionCollectiveRepository conventionCollectiveRepository
    ) {
        this.conventionCollectiveService = conventionCollectiveService;
        this.conventionCollectiveRepository = conventionCollectiveRepository;
    }

    /**
     * {@code POST  /convention-collectives} : Create a new conventionCollective.
     *
     * @param conventionCollective the conventionCollective to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conventionCollective, or with status {@code 400 (Bad Request)} if the conventionCollective has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/convention-collectives")
    public ResponseEntity<ConventionCollective> createConventionCollective(@Valid @RequestBody ConventionCollective conventionCollective)
        throws URISyntaxException {
        log.debug("REST request to save ConventionCollective : {}", conventionCollective);
        if (conventionCollective.getId() != null) {
            throw new BadRequestAlertException("A new conventionCollective cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConventionCollective result = conventionCollectiveService.save(conventionCollective);
        return ResponseEntity
            .created(new URI("/api/convention-collectives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /convention-collectives/:id} : Updates an existing conventionCollective.
     *
     * @param id the id of the conventionCollective to save.
     * @param conventionCollective the conventionCollective to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conventionCollective,
     * or with status {@code 400 (Bad Request)} if the conventionCollective is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conventionCollective couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/convention-collectives/{id}")
    public ResponseEntity<ConventionCollective> updateConventionCollective(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConventionCollective conventionCollective
    ) throws URISyntaxException {
        log.debug("REST request to update ConventionCollective : {}, {}", id, conventionCollective);
        if (conventionCollective.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conventionCollective.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conventionCollectiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConventionCollective result = conventionCollectiveService.update(conventionCollective);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conventionCollective.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /convention-collectives/:id} : Partial updates given fields of an existing conventionCollective, field will ignore if it is null
     *
     * @param id the id of the conventionCollective to save.
     * @param conventionCollective the conventionCollective to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conventionCollective,
     * or with status {@code 400 (Bad Request)} if the conventionCollective is not valid,
     * or with status {@code 404 (Not Found)} if the conventionCollective is not found,
     * or with status {@code 500 (Internal Server Error)} if the conventionCollective couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/convention-collectives/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConventionCollective> partialUpdateConventionCollective(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConventionCollective conventionCollective
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConventionCollective partially : {}, {}", id, conventionCollective);
        if (conventionCollective.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conventionCollective.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conventionCollectiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConventionCollective> result = conventionCollectiveService.partialUpdate(conventionCollective);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conventionCollective.getId().toString())
        );
    }

    /**
     * {@code GET  /convention-collectives} : get all the conventionCollectives.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conventionCollectives in body.
     */
    @GetMapping("/convention-collectives")
    public List<ConventionCollective> getAllConventionCollectives() {
        log.debug("REST request to get all ConventionCollectives");
        return conventionCollectiveService.findAll();
    }

    /**
     * {@code GET  /convention-collectives/:id} : get the "id" conventionCollective.
     *
     * @param id the id of the conventionCollective to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conventionCollective, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/convention-collectives/{id}")
    public ResponseEntity<ConventionCollective> getConventionCollective(@PathVariable Long id) {
        log.debug("REST request to get ConventionCollective : {}", id);
        Optional<ConventionCollective> conventionCollective = conventionCollectiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conventionCollective);
    }

    /**
     * {@code DELETE  /convention-collectives/:id} : delete the "id" conventionCollective.
     *
     * @param id the id of the conventionCollective to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/convention-collectives/{id}")
    public ResponseEntity<Void> deleteConventionCollective(@PathVariable Long id) {
        log.debug("REST request to delete ConventionCollective : {}", id);
        conventionCollectiveService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
