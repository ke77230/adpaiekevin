package fr.adaming.adp.web.rest;

import fr.adaming.adp.domain.Cotisation;
import fr.adaming.adp.repository.CotisationRepository;
import fr.adaming.adp.service.CotisationService;
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
 * REST controller for managing {@link fr.adaming.adp.domain.Cotisation}.
 */
@RestController
@RequestMapping("/api")
public class CotisationResource {

    private final Logger log = LoggerFactory.getLogger(CotisationResource.class);

    private static final String ENTITY_NAME = "cotisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CotisationService cotisationService;

    private final CotisationRepository cotisationRepository;

    public CotisationResource(CotisationService cotisationService, CotisationRepository cotisationRepository) {
        this.cotisationService = cotisationService;
        this.cotisationRepository = cotisationRepository;
    }

    /**
     * {@code POST  /cotisations} : Create a new cotisation.
     *
     * @param cotisation the cotisation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cotisation, or with status {@code 400 (Bad Request)} if the cotisation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cotisations")
    public ResponseEntity<Cotisation> createCotisation(@Valid @RequestBody Cotisation cotisation) throws URISyntaxException {
        log.debug("REST request to save Cotisation : {}", cotisation);
        if (cotisation.getId() != null) {
            throw new BadRequestAlertException("A new cotisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cotisation result = cotisationService.save(cotisation);
        return ResponseEntity
            .created(new URI("/api/cotisations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cotisations/:id} : Updates an existing cotisation.
     *
     * @param id the id of the cotisation to save.
     * @param cotisation the cotisation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cotisation,
     * or with status {@code 400 (Bad Request)} if the cotisation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cotisation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cotisations/{id}")
    public ResponseEntity<Cotisation> updateCotisation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cotisation cotisation
    ) throws URISyntaxException {
        log.debug("REST request to update Cotisation : {}, {}", id, cotisation);
        if (cotisation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cotisation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cotisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cotisation result = cotisationService.update(cotisation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cotisation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cotisations/:id} : Partial updates given fields of an existing cotisation, field will ignore if it is null
     *
     * @param id the id of the cotisation to save.
     * @param cotisation the cotisation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cotisation,
     * or with status {@code 400 (Bad Request)} if the cotisation is not valid,
     * or with status {@code 404 (Not Found)} if the cotisation is not found,
     * or with status {@code 500 (Internal Server Error)} if the cotisation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cotisations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cotisation> partialUpdateCotisation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cotisation cotisation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cotisation partially : {}, {}", id, cotisation);
        if (cotisation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cotisation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cotisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cotisation> result = cotisationService.partialUpdate(cotisation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cotisation.getId().toString())
        );
    }

    /**
     * {@code GET  /cotisations} : get all the cotisations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cotisations in body.
     */
    @GetMapping("/cotisations")
    public List<Cotisation> getAllCotisations() {
        log.debug("REST request to get all Cotisations");
        return cotisationService.findAll();
    }

    /**
     * {@code GET  /cotisations/:id} : get the "id" cotisation.
     *
     * @param id the id of the cotisation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cotisation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cotisations/{id}")
    public ResponseEntity<Cotisation> getCotisation(@PathVariable Long id) {
        log.debug("REST request to get Cotisation : {}", id);
        Optional<Cotisation> cotisation = cotisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cotisation);
    }

    /**
     * {@code DELETE  /cotisations/:id} : delete the "id" cotisation.
     *
     * @param id the id of the cotisation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cotisations/{id}")
    public ResponseEntity<Void> deleteCotisation(@PathVariable Long id) {
        log.debug("REST request to delete Cotisation : {}", id);
        cotisationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
