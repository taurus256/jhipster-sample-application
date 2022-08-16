package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DVTask;
import com.mycompany.myapp.repository.DVTaskRepository;
import com.mycompany.myapp.service.DVTaskService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.DVTask}.
 */
@RestController
@RequestMapping("/api")
public class DVTaskResource {

    private final Logger log = LoggerFactory.getLogger(DVTaskResource.class);

    private static final String ENTITY_NAME = "dVTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DVTaskService dVTaskService;

    private final DVTaskRepository dVTaskRepository;

    public DVTaskResource(DVTaskService dVTaskService, DVTaskRepository dVTaskRepository) {
        this.dVTaskService = dVTaskService;
        this.dVTaskRepository = dVTaskRepository;
    }

    /**
     * {@code POST  /dv-tasks} : Create a new dVTask.
     *
     * @param dVTask the dVTask to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dVTask, or with status {@code 400 (Bad Request)} if the dVTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dv-tasks")
    public ResponseEntity<DVTask> createDVTask(@RequestBody DVTask dVTask) throws URISyntaxException {
        log.debug("REST request to save DVTask : {}", dVTask);
        if (dVTask.getId() != null) {
            throw new BadRequestAlertException("A new dVTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DVTask result = dVTaskService.save(dVTask);
        return ResponseEntity
            .created(new URI("/api/dv-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dv-tasks/:id} : Updates an existing dVTask.
     *
     * @param id the id of the dVTask to save.
     * @param dVTask the dVTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dVTask,
     * or with status {@code 400 (Bad Request)} if the dVTask is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dVTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dv-tasks/{id}")
    public ResponseEntity<DVTask> updateDVTask(@PathVariable(value = "id", required = false) final Long id, @RequestBody DVTask dVTask)
        throws URISyntaxException {
        log.debug("REST request to update DVTask : {}, {}", id, dVTask);
        if (dVTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dVTask.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dVTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DVTask result = dVTaskService.update(dVTask);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dVTask.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dv-tasks/:id} : Partial updates given fields of an existing dVTask, field will ignore if it is null
     *
     * @param id the id of the dVTask to save.
     * @param dVTask the dVTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dVTask,
     * or with status {@code 400 (Bad Request)} if the dVTask is not valid,
     * or with status {@code 404 (Not Found)} if the dVTask is not found,
     * or with status {@code 500 (Internal Server Error)} if the dVTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dv-tasks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DVTask> partialUpdateDVTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DVTask dVTask
    ) throws URISyntaxException {
        log.debug("REST request to partial update DVTask partially : {}, {}", id, dVTask);
        if (dVTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dVTask.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dVTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DVTask> result = dVTaskService.partialUpdate(dVTask);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dVTask.getId().toString())
        );
    }

    /**
     * {@code GET  /dv-tasks} : get all the dVTasks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dVTasks in body.
     */
    @GetMapping("/dv-tasks")
    public ResponseEntity<List<DVTask>> getAllDVTasks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DVTasks");
        Page<DVTask> page = dVTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dv-tasks/:id} : get the "id" dVTask.
     *
     * @param id the id of the dVTask to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dVTask, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dv-tasks/{id}")
    public ResponseEntity<DVTask> getDVTask(@PathVariable Long id) {
        log.debug("REST request to get DVTask : {}", id);
        Optional<DVTask> dVTask = dVTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dVTask);
    }

    /**
     * {@code DELETE  /dv-tasks/:id} : delete the "id" dVTask.
     *
     * @param id the id of the dVTask to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dv-tasks/{id}")
    public ResponseEntity<Void> deleteDVTask(@PathVariable Long id) {
        log.debug("REST request to delete DVTask : {}", id);
        dVTaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
