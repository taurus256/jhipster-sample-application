package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DVJob;
import com.mycompany.myapp.domain.enumeration.BROWSER;
import com.mycompany.myapp.domain.enumeration.OS;
import com.mycompany.myapp.repository.DVJobRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DVJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DVJobResourceIT {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ERROR = false;
    private static final Boolean UPDATED_IS_ERROR = true;

    private static final Boolean DEFAULT_IS_SUCCESS = false;
    private static final Boolean UPDATED_IS_SUCCESS = true;

    private static final OS DEFAULT_OPERATION_SYSTEM = OS.MAC;
    private static final OS UPDATED_OPERATION_SYSTEM = OS.LINUX;

    private static final BROWSER DEFAULT_BROWSER = BROWSER.FIREFOX;
    private static final BROWSER UPDATED_BROWSER = BROWSER.OPERA;

    private static final String ENTITY_API_URL = "/api/dv-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DVJobRepository dVJobRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDVJobMockMvc;

    private DVJob dVJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DVJob createEntity(EntityManager em) {
        DVJob dVJob = new DVJob()
            .uuid(DEFAULT_UUID)
            .createTime(DEFAULT_CREATE_TIME)
            .isError(DEFAULT_IS_ERROR)
            .isSuccess(DEFAULT_IS_SUCCESS)
            .operationSystem(DEFAULT_OPERATION_SYSTEM)
            .browser(DEFAULT_BROWSER);
        return dVJob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DVJob createUpdatedEntity(EntityManager em) {
        DVJob dVJob = new DVJob()
            .uuid(UPDATED_UUID)
            .createTime(UPDATED_CREATE_TIME)
            .isError(UPDATED_IS_ERROR)
            .isSuccess(UPDATED_IS_SUCCESS)
            .operationSystem(UPDATED_OPERATION_SYSTEM)
            .browser(UPDATED_BROWSER);
        return dVJob;
    }

    @BeforeEach
    public void initTest() {
        dVJob = createEntity(em);
    }

    @Test
    @Transactional
    void createDVJob() throws Exception {
        int databaseSizeBeforeCreate = dVJobRepository.findAll().size();
        // Create the DVJob
        restDVJobMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVJob))
            )
            .andExpect(status().isCreated());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeCreate + 1);
        DVJob testDVJob = dVJobList.get(dVJobList.size() - 1);
        assertThat(testDVJob.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testDVJob.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testDVJob.getIsError()).isEqualTo(DEFAULT_IS_ERROR);
        assertThat(testDVJob.getIsSuccess()).isEqualTo(DEFAULT_IS_SUCCESS);
        assertThat(testDVJob.getOperationSystem()).isEqualTo(DEFAULT_OPERATION_SYSTEM);
        assertThat(testDVJob.getBrowser()).isEqualTo(DEFAULT_BROWSER);
    }

    @Test
    @Transactional
    void createDVJobWithExistingId() throws Exception {
        // Create the DVJob with an existing ID
        dVJob.setId(1L);

        int databaseSizeBeforeCreate = dVJobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDVJobMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVJob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDVJobs() throws Exception {
        // Initialize the database
        dVJobRepository.saveAndFlush(dVJob);

        // Get all the dVJobList
        restDVJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dVJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].isError").value(hasItem(DEFAULT_IS_ERROR.booleanValue())))
            .andExpect(jsonPath("$.[*].isSuccess").value(hasItem(DEFAULT_IS_SUCCESS.booleanValue())))
            .andExpect(jsonPath("$.[*].operationSystem").value(hasItem(DEFAULT_OPERATION_SYSTEM.toString())))
            .andExpect(jsonPath("$.[*].browser").value(hasItem(DEFAULT_BROWSER.toString())));
    }

    @Test
    @Transactional
    void getDVJob() throws Exception {
        // Initialize the database
        dVJobRepository.saveAndFlush(dVJob);

        // Get the dVJob
        restDVJobMockMvc
            .perform(get(ENTITY_API_URL_ID, dVJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dVJob.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.isError").value(DEFAULT_IS_ERROR.booleanValue()))
            .andExpect(jsonPath("$.isSuccess").value(DEFAULT_IS_SUCCESS.booleanValue()))
            .andExpect(jsonPath("$.operationSystem").value(DEFAULT_OPERATION_SYSTEM.toString()))
            .andExpect(jsonPath("$.browser").value(DEFAULT_BROWSER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDVJob() throws Exception {
        // Get the dVJob
        restDVJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDVJob() throws Exception {
        // Initialize the database
        dVJobRepository.saveAndFlush(dVJob);

        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();

        // Update the dVJob
        DVJob updatedDVJob = dVJobRepository.findById(dVJob.getId()).get();
        // Disconnect from session so that the updates on updatedDVJob are not directly saved in db
        em.detach(updatedDVJob);
        updatedDVJob
            .uuid(UPDATED_UUID)
            .createTime(UPDATED_CREATE_TIME)
            .isError(UPDATED_IS_ERROR)
            .isSuccess(UPDATED_IS_SUCCESS)
            .operationSystem(UPDATED_OPERATION_SYSTEM)
            .browser(UPDATED_BROWSER);

        restDVJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDVJob.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDVJob))
            )
            .andExpect(status().isOk());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
        DVJob testDVJob = dVJobList.get(dVJobList.size() - 1);
        assertThat(testDVJob.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testDVJob.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDVJob.getIsError()).isEqualTo(UPDATED_IS_ERROR);
        assertThat(testDVJob.getIsSuccess()).isEqualTo(UPDATED_IS_SUCCESS);
        assertThat(testDVJob.getOperationSystem()).isEqualTo(UPDATED_OPERATION_SYSTEM);
        assertThat(testDVJob.getBrowser()).isEqualTo(UPDATED_BROWSER);
    }

    @Test
    @Transactional
    void putNonExistingDVJob() throws Exception {
        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();
        dVJob.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDVJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dVJob.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dVJob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDVJob() throws Exception {
        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();
        dVJob.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dVJob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDVJob() throws Exception {
        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();
        dVJob.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVJobMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVJob))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDVJobWithPatch() throws Exception {
        // Initialize the database
        dVJobRepository.saveAndFlush(dVJob);

        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();

        // Update the dVJob using partial update
        DVJob partialUpdatedDVJob = new DVJob();
        partialUpdatedDVJob.setId(dVJob.getId());

        partialUpdatedDVJob
            .createTime(UPDATED_CREATE_TIME)
            .isError(UPDATED_IS_ERROR)
            .isSuccess(UPDATED_IS_SUCCESS)
            .operationSystem(UPDATED_OPERATION_SYSTEM);

        restDVJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDVJob.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDVJob))
            )
            .andExpect(status().isOk());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
        DVJob testDVJob = dVJobList.get(dVJobList.size() - 1);
        assertThat(testDVJob.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testDVJob.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDVJob.getIsError()).isEqualTo(UPDATED_IS_ERROR);
        assertThat(testDVJob.getIsSuccess()).isEqualTo(UPDATED_IS_SUCCESS);
        assertThat(testDVJob.getOperationSystem()).isEqualTo(UPDATED_OPERATION_SYSTEM);
        assertThat(testDVJob.getBrowser()).isEqualTo(DEFAULT_BROWSER);
    }

    @Test
    @Transactional
    void fullUpdateDVJobWithPatch() throws Exception {
        // Initialize the database
        dVJobRepository.saveAndFlush(dVJob);

        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();

        // Update the dVJob using partial update
        DVJob partialUpdatedDVJob = new DVJob();
        partialUpdatedDVJob.setId(dVJob.getId());

        partialUpdatedDVJob
            .uuid(UPDATED_UUID)
            .createTime(UPDATED_CREATE_TIME)
            .isError(UPDATED_IS_ERROR)
            .isSuccess(UPDATED_IS_SUCCESS)
            .operationSystem(UPDATED_OPERATION_SYSTEM)
            .browser(UPDATED_BROWSER);

        restDVJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDVJob.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDVJob))
            )
            .andExpect(status().isOk());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
        DVJob testDVJob = dVJobList.get(dVJobList.size() - 1);
        assertThat(testDVJob.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testDVJob.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDVJob.getIsError()).isEqualTo(UPDATED_IS_ERROR);
        assertThat(testDVJob.getIsSuccess()).isEqualTo(UPDATED_IS_SUCCESS);
        assertThat(testDVJob.getOperationSystem()).isEqualTo(UPDATED_OPERATION_SYSTEM);
        assertThat(testDVJob.getBrowser()).isEqualTo(UPDATED_BROWSER);
    }

    @Test
    @Transactional
    void patchNonExistingDVJob() throws Exception {
        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();
        dVJob.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDVJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dVJob.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVJob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDVJob() throws Exception {
        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();
        dVJob.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVJob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDVJob() throws Exception {
        int databaseSizeBeforeUpdate = dVJobRepository.findAll().size();
        dVJob.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVJobMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVJob))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DVJob in the database
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDVJob() throws Exception {
        // Initialize the database
        dVJobRepository.saveAndFlush(dVJob);

        int databaseSizeBeforeDelete = dVJobRepository.findAll().size();

        // Delete the dVJob
        restDVJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, dVJob.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DVJob> dVJobList = dVJobRepository.findAll();
        assertThat(dVJobList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
