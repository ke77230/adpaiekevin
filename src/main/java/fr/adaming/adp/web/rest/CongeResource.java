package fr.adaming.adp.web.rest;

import fr.adaming.adp.domain.Conge;
import fr.adaming.adp.repository.CongeRepository;
import fr.adaming.adp.service.CongeService;
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
 * REST controller for managing {@link fr.adaming.adp.domain.Conge}.
 */
@RestController
@RequestMapping("/api")
public class CongeResource {

    private final Logger log = LoggerFactory.getLogger(CongeResource.class);

    private static final String ENTITY_NAME = "conge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CongeService congeService;

    private final CongeRepository congeRepository;

    public CongeResource(CongeService congeService, CongeRepository congeRepository) {
        this.congeService = congeService;
        this.congeRepository = congeRepository;
    }

    /**
     * {@code POST  /conges} : Create a new conge.
     *
     * @param conge the conge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conge, or with status {@code 400 (Bad Request)} if the conge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conges")
    public ResponseEntity<Conge> createConge(@Valid @RequestBody Conge conge) throws URISyntaxException {
        log.debug("REST request to save Conge : {}", conge);
        if (conge.getId() != null) {
            throw new BadRequestAlertException("A new conge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conge result = congeService.save(conge);
        return ResponseEntity
            .created(new URI("/api/conges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conges/:id} : Updates an existing conge.
     *
     * @param id the id of the conge to save.
     * @param conge the conge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conge,
     * or with status {@code 400 (Bad Request)} if the conge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conges/{id}")
    public ResponseEntity<Conge> updateConge(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Conge conge)
        throws URISyntaxException {
        log.debug("REST request to update Conge : {}, {}", id, conge);
        if (conge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!congeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Conge result = congeService.update(conge);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conge.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conges/:id} : Partial updates given fields of an existing conge, field will ignore if it is null
     *
     * @param id the id of the conge to save.
     * @param conge the conge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conge,
     * or with status {@code 400 (Bad Request)} if the conge is not valid,
     * or with status {@code 404 (Not Found)} if the conge is not found,
     * or with status {@code 500 (Internal Server Error)} if the conge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Conge> partialUpdateConge(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Conge conge
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conge partially : {}, {}", id, conge);
        if (conge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!congeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Conge> result = congeService.partialUpdate(conge);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conge.getId().toString())
        );
    }

    /**
     * {@code GET  /conges} : get all the conges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conges in body.
     */
    @GetMapping("/conges")
    public List<Conge> getAllConges() {
        log.debug("REST request to get all Conges");
        return congeService.findAll();
    }

    /**
     * {@code GET  /conges/:id} : get the "id" conge.
     *
     * @param id the id of the conge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conges/{id}")
    public ResponseEntity<Conge> getConge(@PathVariable Long id) {
        log.debug("REST request to get Conge : {}", id);
        Optional<Conge> conge = congeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conge);
    }

    /**
     * {@code DELETE  /conges/:id} : delete the "id" conge.
     *
     * @param id the id of the conge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conges/{id}")
    public ResponseEntity<Void> deleteConge(@PathVariable Long id) {
        log.debug("REST request to delete Conge : {}", id);
        congeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
