package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DVTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DVTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DVTaskRepository extends JpaRepository<DVTask, Long> {}
