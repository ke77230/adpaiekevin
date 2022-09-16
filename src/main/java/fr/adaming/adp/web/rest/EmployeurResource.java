package fr.adaming.adp.web.rest;

import fr.adaming.adp.domain.Employeur;
import fr.adaming.adp.repository.EmployeurRepository;
import fr.adaming.adp.service.EmployeurService;
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
 * REST controller for managing {@link fr.adaming.adp.domain.Employeur}.
 */
@RestController
@RequestMapping("/api")
public class EmployeurResource {

    private final Logger log = LoggerFactory.getLogger(EmployeurResource.class);

    private static final String ENTITY_NAME = "employeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeurService employeurService;

    private final EmployeurRepository employeurRepository;

    public EmployeurResource(EmployeurService employeurService, EmployeurRepository employeurRepository) {
        this.employeurService = employeurService;
        this.employeurRepository = employeurRepository;
    }

    /**
     * {@code POST  /employeurs} : Create a new employeur.
     *
     * @param employeur the employeur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeur, or with status {@code 400 (Bad Request)} if the employeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employeurs")
    public ResponseEntity<Employeur> createEmployeur(@Valid @RequestBody Employeur employeur) throws URISyntaxException {
        log.debug("REST request to save Employeur : {}", employeur);
        if (employeur.getId() != null) {
            throw new BadRequestAlertException("A new employeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Employeur result = employeurService.save(employeur);
        return ResponseEntity
            .created(new URI("/api/employeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employeurs/:id} : Updates an existing employeur.
     *
     * @param id the id of the employeur to save.
     * @param employeur the employeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeur,
     * or with status {@code 400 (Bad Request)} if the employeur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employeurs/{id}")
    public ResponseEntity<Employeur> updateEmployeur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Employeur employeur
    ) throws URISyntaxException {
        log.debug("REST request to update Employeur : {}, {}", id, employeur);
        if (employeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Employeur result = employeurService.update(employeur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employeurs/:id} : Partial updates given fields of an existing employeur, field will ignore if it is null
     *
     * @param id the id of the employeur to save.
     * @param employeur the employeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeur,
     * or with status {@code 400 (Bad Request)} if the employeur is not valid,
     * or with status {@code 404 (Not Found)} if the employeur is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employeurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Employeur> partialUpdateEmployeur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Employeur employeur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Employeur partially : {}, {}", id, employeur);
        if (employeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Employeur> result = employeurService.partialUpdate(employeur);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeur.getId().toString())
        );
    }

    /**
     * {@code GET  /employeurs} : get all the employeurs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeurs in body.
     */
    @GetMapping("/employeurs")
    public List<Employeur> getAllEmployeurs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Employeurs");
        return employeurService.findAll();
    }

    /**
     * {@code GET  /employeurs/:id} : get the "id" employeur.
     *
     * @param id the id of the employeur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employeurs/{id}")
    public ResponseEntity<Employeur> getEmployeur(@PathVariable Long id) {
        log.debug("REST request to get Employeur : {}", id);
        Optional<Employeur> employeur = employeurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeur);
    }

    /**
     * {@code DELETE  /employeurs/:id} : delete the "id" employeur.
     *
     * @param id the id of the employeur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employeurs/{id}")
    public ResponseEntity<Void> deleteEmployeur(@PathVariable Long id) {
        log.debug("REST request to delete Employeur : {}", id);
        employeurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
