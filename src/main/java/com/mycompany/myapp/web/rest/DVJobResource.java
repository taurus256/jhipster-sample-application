package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DVJob;
import com.mycompany.myapp.repository.DVJobRepository;
import com.mycompany.myapp.service.DVJobService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.DVJob}.
 */
@RestController
@RequestMapping("/api")
public class DVJobResource {

    private final Logger log = LoggerFactory.getLogger(DVJobResource.class);

    private static final String ENTITY_NAME = "dVJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DVJobService dVJobService;

    private final DVJobRepository dVJobRepository;

    public DVJobResource(DVJobService dVJobService, DVJobRepository dVJobRepository) {
        this.dVJobService = dVJobService;
        this.dVJobRepository = dVJobRepository;
    }

    /**
     * {@code POST  /dv-jobs} : Create a new dVJob.
     *
     * @param dVJob the dVJob to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dVJob, or with status {@code 400 (Bad Request)} if the dVJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dv-jobs")
    public ResponseEntity<DVJob> createDVJob(@RequestBody DVJob dVJob) throws URISyntaxException {
        log.debug("REST request to save DVJob : {}", dVJob);
        if (dVJob.getId() != null) {
            throw new BadRequestAlertException("A new dVJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DVJob result = dVJobService.save(dVJob);
        return ResponseEntity
            .created(new URI("/api/dv-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dv-jobs/:id} : Updates an existing dVJob.
     *
     * @param id the id of the dVJob to save.
     * @param dVJob the dVJob to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dVJob,
     * or with status {@code 400 (Bad Request)} if the dVJob is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dVJob couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dv-jobs/{id}")
    public ResponseEntity<DVJob> updateDVJob(@PathVariable(value = "id", required = false) final Long id, @RequestBody DVJob dVJob)
        throws URISyntaxException {
        log.debug("REST request to update DVJob : {}, {}", id, dVJob);
        if (dVJob.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dVJob.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dVJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DVJob result = dVJobService.update(dVJob);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dVJob.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dv-jobs/:id} : Partial updates given fields of an existing dVJob, field will ignore if it is null
     *
     * @param id the id of the dVJob to save.
     * @param dVJob the dVJob to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dVJob,
     * or with status {@code 400 (Bad Request)} if the dVJob is not valid,
     * or with status {@code 404 (Not Found)} if the dVJob is not found,
     * or with status {@code 500 (Internal Server Error)} if the dVJob couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dv-jobs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DVJob> partialUpdateDVJob(@PathVariable(value = "id", required = false) final Long id, @RequestBody DVJob dVJob)
        throws URISyntaxException {
        log.debug("REST request to partial update DVJob partially : {}, {}", id, dVJob);
        if (dVJob.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dVJob.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dVJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DVJob> result = dVJobService.partialUpdate(dVJob);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dVJob.getId().toString())
        );
    }

    /**
     * {@code GET  /dv-jobs} : get all the dVJobs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dVJobs in body.
     */
    @GetMapping("/dv-jobs")
    public ResponseEntity<List<DVJob>> getAllDVJobs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DVJobs");
        Page<DVJob> page = dVJobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dv-jobs/:id} : get the "id" dVJob.
     *
     * @param id the id of the dVJob to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dVJob, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dv-jobs/{id}")
    public ResponseEntity<DVJob> getDVJob(@PathVariable Long id) {
        log.debug("REST request to get DVJob : {}", id);
        Optional<DVJob> dVJob = dVJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dVJob);
    }

    /**
     * {@code DELETE  /dv-jobs/:id} : delete the "id" dVJob.
     *
     * @param id the id of the dVJob to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dv-jobs/{id}")
    public ResponseEntity<Void> deleteDVJob(@PathVariable Long id) {
        log.debug("REST request to delete DVJob : {}", id);
        dVJobService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
