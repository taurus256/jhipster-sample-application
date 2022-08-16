package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.DVUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DVUser}.
 */
public interface DVUserService {
    /**
     * Save a dVUser.
     *
     * @param dVUser the entity to save.
     * @return the persisted entity.
     */
    DVUser save(DVUser dVUser);

    /**
     * Updates a dVUser.
     *
     * @param dVUser the entity to update.
     * @return the persisted entity.
     */
    DVUser update(DVUser dVUser);

    /**
     * Partially updates a dVUser.
     *
     * @param dVUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DVUser> partialUpdate(DVUser dVUser);

    /**
     * Get all the dVUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DVUser> findAll(Pageable pageable);

    /**
     * Get the "id" dVUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DVUser> findOne(Long id);

    /**
     * Delete the "id" dVUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
