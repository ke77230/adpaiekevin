package fr.adaming.adp.web.rest;

import fr.adaming.adp.domain.FicheDePaie;
import fr.adaming.adp.repository.FicheDePaieRepository;
import fr.adaming.adp.service.FicheDePaieService;
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
 * REST controller for managing {@link fr.adaming.adp.domain.FicheDePaie}.
 */
@RestController
@RequestMapping("/api")
public class FicheDePaieResource {

    private final Logger log = LoggerFactory.getLogger(FicheDePaieResource.class);

    private static final String ENTITY_NAME = "ficheDePaie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FicheDePaieService ficheDePaieService;

    private final FicheDePaieRepository ficheDePaieRepository;

    public FicheDePaieResource(FicheDePaieService ficheDePaieService, FicheDePaieRepository ficheDePaieRepository) {
        this.ficheDePaieService = ficheDePaieService;
        this.ficheDePaieRepository = ficheDePaieRepository;
    }

    /**
     * {@code POST  /fiche-de-paies} : Create a new ficheDePaie.
     *
     * @param ficheDePaie the ficheDePaie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ficheDePaie, or with status {@code 400 (Bad Request)} if the ficheDePaie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fiche-de-paies")
    public ResponseEntity<FicheDePaie> createFicheDePaie(@Valid @RequestBody FicheDePaie ficheDePaie) throws URISyntaxException {
        log.debug("REST request to save FicheDePaie : {}", ficheDePaie);
        if (ficheDePaie.getId() != null) {
            throw new BadRequestAlertException("A new ficheDePaie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FicheDePaie result = ficheDePaieService.save(ficheDePaie);
        return ResponseEntity
            .created(new URI("/api/fiche-de-paies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fiche-de-paies/:id} : Updates an existing ficheDePaie.
     *
     * @param id the id of the ficheDePaie to save.
     * @param ficheDePaie the ficheDePaie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ficheDePaie,
     * or with status {@code 400 (Bad Request)} if the ficheDePaie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ficheDePaie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fiche-de-paies/{id}")
    public ResponseEntity<FicheDePaie> updateFicheDePaie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FicheDePaie ficheDePaie
    ) throws URISyntaxException {
        log.debug("REST request to update FicheDePaie : {}, {}", id, ficheDePaie);
        if (ficheDePaie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ficheDePaie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ficheDePaieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FicheDePaie result = ficheDePaieService.update(ficheDePaie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ficheDePaie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fiche-de-paies/:id} : Partial updates given fields of an existing ficheDePaie, field will ignore if it is null
     *
     * @param id the id of the ficheDePaie to save.
     * @param ficheDePaie the ficheDePaie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ficheDePaie,
     * or with status {@code 400 (Bad Request)} if the ficheDePaie is not valid,
     * or with status {@code 404 (Not Found)} if the ficheDePaie is not found,
     * or with status {@code 500 (Internal Server Error)} if the ficheDePaie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fiche-de-paies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FicheDePaie> partialUpdateFicheDePaie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FicheDePaie ficheDePaie
    ) throws URISyntaxException {
        log.debug("REST request to partial update FicheDePaie partially : {}, {}", id, ficheDePaie);
        if (ficheDePaie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ficheDePaie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ficheDePaieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FicheDePaie> result = ficheDePaieService.partialUpdate(ficheDePaie);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ficheDePaie.getId().toString())
        );
    }

    /**
     * {@code GET  /fiche-de-paies} : get all the ficheDePaies.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ficheDePaies in body.
     */
    @GetMapping("/fiche-de-paies")
    public List<FicheDePaie> getAllFicheDePaies(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all FicheDePaies");
        return ficheDePaieService.findAll();
    }

    /**
     * {@code GET  /fiche-de-paies/:id} : get the "id" ficheDePaie.
     *
     * @param id the id of the ficheDePaie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ficheDePaie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fiche-de-paies/{id}")
    public ResponseEntity<FicheDePaie> getFicheDePaie(@PathVariable Long id) {
        log.debug("REST request to get FicheDePaie : {}", id);
        Optional<FicheDePaie> ficheDePaie = ficheDePaieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ficheDePaie);
    }

    /**
     * {@code DELETE  /fiche-de-paies/:id} : delete the "id" ficheDePaie.
     *
     * @param id the id of the ficheDePaie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fiche-de-paies/{id}")
    public ResponseEntity<Void> deleteFicheDePaie(@PathVariable Long id) {
        log.debug("REST request to delete FicheDePaie : {}", id);
        ficheDePaieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
