package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DVUser;
import com.mycompany.myapp.repository.DVUserRepository;
import com.mycompany.myapp.service.DVUserService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.DVUser}.
 */
@RestController
@RequestMapping("/api")
public class DVUserResource {

    private final Logger log = LoggerFactory.getLogger(DVUserResource.class);

    private static final String ENTITY_NAME = "dVUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DVUserService dVUserService;

    private final DVUserRepository dVUserRepository;

    public DVUserResource(DVUserService dVUserService, DVUserRepository dVUserRepository) {
        this.dVUserService = dVUserService;
        this.dVUserRepository = dVUserRepository;
    }

    /**
     * {@code POST  /dv-users} : Create a new dVUser.
     *
     * @param dVUser the dVUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dVUser, or with status {@code 400 (Bad Request)} if the dVUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dv-users")
    public ResponseEntity<DVUser> createDVUser(@RequestBody DVUser dVUser) throws URISyntaxException {
        log.debug("REST request to save DVUser : {}", dVUser);
        if (dVUser.getId() != null) {
            throw new BadRequestAlertException("A new dVUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DVUser result = dVUserService.save(dVUser);
        return ResponseEntity
            .created(new URI("/api/dv-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dv-users/:id} : Updates an existing dVUser.
     *
     * @param id the id of the dVUser to save.
     * @param dVUser the dVUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dVUser,
     * or with status {@code 400 (Bad Request)} if the dVUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dVUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dv-users/{id}")
    public ResponseEntity<DVUser> updateDVUser(@PathVariable(value = "id", required = false) final Long id, @RequestBody DVUser dVUser)
        throws URISyntaxException {
        log.debug("REST request to update DVUser : {}, {}", id, dVUser);
        if (dVUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dVUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dVUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DVUser result = dVUserService.update(dVUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dVUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dv-users/:id} : Partial updates given fields of an existing dVUser, field will ignore if it is null
     *
     * @param id the id of the dVUser to save.
     * @param dVUser the dVUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dVUser,
     * or with status {@code 400 (Bad Request)} if the dVUser is not valid,
     * or with status {@code 404 (Not Found)} if the dVUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the dVUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dv-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DVUser> partialUpdateDVUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DVUser dVUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update DVUser partially : {}, {}", id, dVUser);
        if (dVUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dVUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dVUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DVUser> result = dVUserService.partialUpdate(dVUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dVUser.getId().toString())
        );
    }

    /**
     * {@code GET  /dv-users} : get all the dVUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dVUsers in body.
     */
    @GetMapping("/dv-users")
    public ResponseEntity<List<DVUser>> getAllDVUsers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DVUsers");
        Page<DVUser> page = dVUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dv-users/:id} : get the "id" dVUser.
     *
     * @param id the id of the dVUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dVUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dv-users/{id}")
    public ResponseEntity<DVUser> getDVUser(@PathVariable Long id) {
        log.debug("REST request to get DVUser : {}", id);
        Optional<DVUser> dVUser = dVUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dVUser);
    }

    /**
     * {@code DELETE  /dv-users/:id} : delete the "id" dVUser.
     *
     * @param id the id of the dVUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dv-users/{id}")
    public ResponseEntity<Void> deleteDVUser(@PathVariable Long id) {
        log.debug("REST request to delete DVUser : {}", id);
        dVUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
