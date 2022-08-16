package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.DVJob;
import com.mycompany.myapp.repository.DVJobRepository;
import com.mycompany.myapp.service.DVJobService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DVJob}.
 */
@Service
@Transactional
public class DVJobServiceImpl implements DVJobService {

    private final Logger log = LoggerFactory.getLogger(DVJobServiceImpl.class);

    private final DVJobRepository dVJobRepository;

    public DVJobServiceImpl(DVJobRepository dVJobRepository) {
        this.dVJobRepository = dVJobRepository;
    }

    @Override
    public DVJob save(DVJob dVJob) {
        log.debug("Request to save DVJob : {}", dVJob);
        return dVJobRepository.save(dVJob);
    }

    @Override
    public DVJob update(DVJob dVJob) {
        log.debug("Request to save DVJob : {}", dVJob);
        return dVJobRepository.save(dVJob);
    }

    @Override
    public Optional<DVJob> partialUpdate(DVJob dVJob) {
        log.debug("Request to partially update DVJob : {}", dVJob);

        return dVJobRepository
            .findById(dVJob.getId())
            .map(existingDVJob -> {
                if (dVJob.getUuid() != null) {
                    existingDVJob.setUuid(dVJob.getUuid());
                }
                if (dVJob.getCreateTime() != null) {
                    existingDVJob.setCreateTime(dVJob.getCreateTime());
                }
                if (dVJob.getIsError() != null) {
                    existingDVJob.setIsError(dVJob.getIsError());
                }
                if (dVJob.getIsSuccess() != null) {
                    existingDVJob.setIsSuccess(dVJob.getIsSuccess());
                }
                if (dVJob.getOperationSystem() != null) {
                    existingDVJob.setOperationSystem(dVJob.getOperationSystem());
                }
                if (dVJob.getBrowser() != null) {
                    existingDVJob.setBrowser(dVJob.getBrowser());
                }

                return existingDVJob;
            })
            .map(dVJobRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DVJob> findAll(Pageable pageable) {
        log.debug("Request to get all DVJobs");
        return dVJobRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DVJob> findOne(Long id) {
        log.debug("Request to get DVJob : {}", id);
        return dVJobRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DVJob : {}", id);
        dVJobRepository.deleteById(id);
    }
}
