package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.DVTask;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DVTask}.
 */
public interface DVTaskService {
    /**
     * Save a dVTask.
     *
     * @param dVTask the entity to save.
     * @return the persisted entity.
     */
    DVTask save(DVTask dVTask);

    /**
     * Updates a dVTask.
     *
     * @param dVTask the entity to update.
     * @return the persisted entity.
     */
    DVTask update(DVTask dVTask);

    /**
     * Partially updates a dVTask.
     *
     * @param dVTask the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DVTask> partialUpdate(DVTask dVTask);

    /**
     * Get all the dVTasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DVTask> findAll(Pageable pageable);

    /**
     * Get the "id" dVTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DVTask> findOne(Long id);

    /**
     * Delete the "id" dVTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
