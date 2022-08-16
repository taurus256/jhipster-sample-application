package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.DVUser;
import com.mycompany.myapp.repository.DVUserRepository;
import com.mycompany.myapp.service.DVUserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DVUser}.
 */
@Service
@Transactional
public class DVUserServiceImpl implements DVUserService {

    private final Logger log = LoggerFactory.getLogger(DVUserServiceImpl.class);

    private final DVUserRepository dVUserRepository;

    public DVUserServiceImpl(DVUserRepository dVUserRepository) {
        this.dVUserRepository = dVUserRepository;
    }

    @Override
    public DVUser save(DVUser dVUser) {
        log.debug("Request to save DVUser : {}", dVUser);
        return dVUserRepository.save(dVUser);
    }

    @Override
    public DVUser update(DVUser dVUser) {
        log.debug("Request to save DVUser : {}", dVUser);
        return dVUserRepository.save(dVUser);
    }

    @Override
    public Optional<DVUser> partialUpdate(DVUser dVUser) {
        log.debug("Request to partially update DVUser : {}", dVUser);

        return dVUserRepository
            .findById(dVUser.getId())
            .map(existingDVUser -> {
                if (dVUser.getName() != null) {
                    existingDVUser.setName(dVUser.getName());
                }
                if (dVUser.getEmail() != null) {
                    existingDVUser.setEmail(dVUser.getEmail());
                }

                return existingDVUser;
            })
            .map(dVUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DVUser> findAll(Pageable pageable) {
        log.debug("Request to get all DVUsers");
        return dVUserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DVUser> findOne(Long id) {
        log.debug("Request to get DVUser : {}", id);
        return dVUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DVUser : {}", id);
        dVUserRepository.deleteById(id);
    }
}
