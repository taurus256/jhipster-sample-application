package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.DVJob;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DVJob}.
 */
public interface DVJobService {
    /**
     * Save a dVJob.
     *
     * @param dVJob the entity to save.
     * @return the persisted entity.
     */
    DVJob save(DVJob dVJob);

    /**
     * Updates a dVJob.
     *
     * @param dVJob the entity to update.
     * @return the persisted entity.
     */
    DVJob update(DVJob dVJob);

    /**
     * Partially updates a dVJob.
     *
     * @param dVJob the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DVJob> partialUpdate(DVJob dVJob);

    /**
     * Get all the dVJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DVJob> findAll(Pageable pageable);

    /**
     * Get the "id" dVJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DVJob> findOne(Long id);

    /**
     * Delete the "id" dVJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
