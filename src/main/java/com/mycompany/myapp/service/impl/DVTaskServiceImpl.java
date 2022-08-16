package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.DVTask;
import com.mycompany.myapp.repository.DVTaskRepository;
import com.mycompany.myapp.service.DVTaskService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DVTask}.
 */
@Service
@Transactional
public class DVTaskServiceImpl implements DVTaskService {

    private final Logger log = LoggerFactory.getLogger(DVTaskServiceImpl.class);

    private final DVTaskRepository dVTaskRepository;

    public DVTaskServiceImpl(DVTaskRepository dVTaskRepository) {
        this.dVTaskRepository = dVTaskRepository;
    }

    @Override
    public DVTask save(DVTask dVTask) {
        log.debug("Request to save DVTask : {}", dVTask);
        return dVTaskRepository.save(dVTask);
    }

    @Override
    public DVTask update(DVTask dVTask) {
        log.debug("Request to save DVTask : {}", dVTask);
        return dVTaskRepository.save(dVTask);
    }

    @Override
    public Optional<DVTask> partialUpdate(DVTask dVTask) {
        log.debug("Request to partially update DVTask : {}", dVTask);

        return dVTaskRepository
            .findById(dVTask.getId())
            .map(existingDVTask -> {
                if (dVTask.getUuid() != null) {
                    existingDVTask.setUuid(dVTask.getUuid());
                }
                if (dVTask.getCreateTime() != null) {
                    existingDVTask.setCreateTime(dVTask.getCreateTime());
                }
                if (dVTask.getRetryCounter() != null) {
                    existingDVTask.setRetryCounter(dVTask.getRetryCounter());
                }
                if (dVTask.getErrorDesc() != null) {
                    existingDVTask.setErrorDesc(dVTask.getErrorDesc());
                }
                if (dVTask.getOperationSystem() != null) {
                    existingDVTask.setOperationSystem(dVTask.getOperationSystem());
                }
                if (dVTask.getBrowser() != null) {
                    existingDVTask.setBrowser(dVTask.getBrowser());
                }
                if (dVTask.getResolutionWidth() != null) {
                    existingDVTask.setResolutionWidth(dVTask.getResolutionWidth());
                }
                if (dVTask.getResolutionHeight() != null) {
                    existingDVTask.setResolutionHeight(dVTask.getResolutionHeight());
                }

                return existingDVTask;
            })
            .map(dVTaskRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DVTask> findAll(Pageable pageable) {
        log.debug("Request to get all DVTasks");
        return dVTaskRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DVTask> findOne(Long id) {
        log.debug("Request to get DVTask : {}", id);
        return dVTaskRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DVTask : {}", id);
        dVTaskRepository.deleteById(id);
    }
}
