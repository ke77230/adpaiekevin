package fr.adaming.adp.web.rest;

import fr.adaming.adp.domain.TauxDImposition;
import fr.adaming.adp.repository.TauxDImpositionRepository;
import fr.adaming.adp.service.TauxDImpositionService;
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
 * REST controller for managing {@link fr.adaming.adp.domain.TauxDImposition}.
 */
@RestController
@RequestMapping("/api")
public class TauxDImpositionResource {

    private final Logger log = LoggerFactory.getLogger(TauxDImpositionResource.class);

    private static final String ENTITY_NAME = "tauxDImposition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TauxDImpositionService tauxDImpositionService;

    private final TauxDImpositionRepository tauxDImpositionRepository;

    public TauxDImpositionResource(TauxDImpositionService tauxDImpositionService, TauxDImpositionRepository tauxDImpositionRepository) {
        this.tauxDImpositionService = tauxDImpositionService;
        this.tauxDImpositionRepository = tauxDImpositionRepository;
    }

    /**
     * {@code POST  /taux-d-impositions} : Create a new tauxDImposition.
     *
     * @param tauxDImposition the tauxDImposition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tauxDImposition, or with status {@code 400 (Bad Request)} if the tauxDImposition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/taux-d-impositions")
    public ResponseEntity<TauxDImposition> createTauxDImposition(@Valid @RequestBody TauxDImposition tauxDImposition)
        throws URISyntaxException {
        log.debug("REST request to save TauxDImposition : {}", tauxDImposition);
        if (tauxDImposition.getId() != null) {
            throw new BadRequestAlertException("A new tauxDImposition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TauxDImposition result = tauxDImpositionService.save(tauxDImposition);
        return ResponseEntity
            .created(new URI("/api/taux-d-impositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /taux-d-impositions/:id} : Updates an existing tauxDImposition.
     *
     * @param id the id of the tauxDImposition to save.
     * @param tauxDImposition the tauxDImposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tauxDImposition,
     * or with status {@code 400 (Bad Request)} if the tauxDImposition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tauxDImposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/taux-d-impositions/{id}")
    public ResponseEntity<TauxDImposition> updateTauxDImposition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TauxDImposition tauxDImposition
    ) throws URISyntaxException {
        log.debug("REST request to update TauxDImposition : {}, {}", id, tauxDImposition);
        if (tauxDImposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tauxDImposition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tauxDImpositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TauxDImposition result = tauxDImpositionService.update(tauxDImposition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tauxDImposition.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /taux-d-impositions/:id} : Partial updates given fields of an existing tauxDImposition, field will ignore if it is null
     *
     * @param id the id of the tauxDImposition to save.
     * @param tauxDImposition the tauxDImposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tauxDImposition,
     * or with status {@code 400 (Bad Request)} if the tauxDImposition is not valid,
     * or with status {@code 404 (Not Found)} if the tauxDImposition is not found,
     * or with status {@code 500 (Internal Server Error)} if the tauxDImposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/taux-d-impositions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TauxDImposition> partialUpdateTauxDImposition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TauxDImposition tauxDImposition
    ) throws URISyntaxException {
        log.debug("REST request to partial update TauxDImposition partially : {}, {}", id, tauxDImposition);
        if (tauxDImposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tauxDImposition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tauxDImpositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TauxDImposition> result = tauxDImpositionService.partialUpdate(tauxDImposition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tauxDImposition.getId().toString())
        );
    }

    /**
     * {@code GET  /taux-d-impositions} : get all the tauxDImpositions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tauxDImpositions in body.
     */
    @GetMapping("/taux-d-impositions")
    public List<TauxDImposition> getAllTauxDImpositions() {
        log.debug("REST request to get all TauxDImpositions");
        return tauxDImpositionService.findAll();
    }

    /**
     * {@code GET  /taux-d-impositions/:id} : get the "id" tauxDImposition.
     *
     * @param id the id of the tauxDImposition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tauxDImposition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/taux-d-impositions/{id}")
    public ResponseEntity<TauxDImposition> getTauxDImposition(@PathVariable Long id) {
        log.debug("REST request to get TauxDImposition : {}", id);
        Optional<TauxDImposition> tauxDImposition = tauxDImpositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tauxDImposition);
    }

    /**
     * {@code DELETE  /taux-d-impositions/:id} : delete the "id" tauxDImposition.
     *
     * @param id the id of the tauxDImposition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/taux-d-impositions/{id}")
    public ResponseEntity<Void> deleteTauxDImposition(@PathVariable Long id) {
        log.debug("REST request to delete TauxDImposition : {}", id);
        tauxDImpositionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
