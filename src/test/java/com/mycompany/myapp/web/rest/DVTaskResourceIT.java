package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DVTask;
import com.mycompany.myapp.domain.enumeration.BROWSER;
import com.mycompany.myapp.domain.enumeration.OS;
import com.mycompany.myapp.repository.DVTaskRepository;
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
 * Integration tests for the {@link DVTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DVTaskResourceIT {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RETRY_COUNTER = 1;
    private static final Integer UPDATED_RETRY_COUNTER = 2;

    private static final String DEFAULT_ERROR_DESC = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_DESC = "BBBBBBBBBB";

    private static final OS DEFAULT_OPERATION_SYSTEM = OS.MAC;
    private static final OS UPDATED_OPERATION_SYSTEM = OS.LINUX;

    private static final BROWSER DEFAULT_BROWSER = BROWSER.FIREFOX;
    private static final BROWSER UPDATED_BROWSER = BROWSER.OPERA;

    private static final Integer DEFAULT_RESOLUTION_WIDTH = 1;
    private static final Integer UPDATED_RESOLUTION_WIDTH = 2;

    private static final Integer DEFAULT_RESOLUTION_HEIGHT = 1;
    private static final Integer UPDATED_RESOLUTION_HEIGHT = 2;

    private static final String ENTITY_API_URL = "/api/dv-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DVTaskRepository dVTaskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDVTaskMockMvc;

    private DVTask dVTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DVTask createEntity(EntityManager em) {
        DVTask dVTask = new DVTask()
            .uuid(DEFAULT_UUID)
            .createTime(DEFAULT_CREATE_TIME)
            .retryCounter(DEFAULT_RETRY_COUNTER)
            .errorDesc(DEFAULT_ERROR_DESC)
            .operationSystem(DEFAULT_OPERATION_SYSTEM)
            .browser(DEFAULT_BROWSER)
            .resolutionWidth(DEFAULT_RESOLUTION_WIDTH)
            .resolutionHeight(DEFAULT_RESOLUTION_HEIGHT);
        return dVTask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DVTask createUpdatedEntity(EntityManager em) {
        DVTask dVTask = new DVTask()
            .uuid(UPDATED_UUID)
            .createTime(UPDATED_CREATE_TIME)
            .retryCounter(UPDATED_RETRY_COUNTER)
            .errorDesc(UPDATED_ERROR_DESC)
            .operationSystem(UPDATED_OPERATION_SYSTEM)
            .browser(UPDATED_BROWSER)
            .resolutionWidth(UPDATED_RESOLUTION_WIDTH)
            .resolutionHeight(UPDATED_RESOLUTION_HEIGHT);
        return dVTask;
    }

    @BeforeEach
    public void initTest() {
        dVTask = createEntity(em);
    }

    @Test
    @Transactional
    void createDVTask() throws Exception {
        int databaseSizeBeforeCreate = dVTaskRepository.findAll().size();
        // Create the DVTask
        restDVTaskMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVTask))
            )
            .andExpect(status().isCreated());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeCreate + 1);
        DVTask testDVTask = dVTaskList.get(dVTaskList.size() - 1);
        assertThat(testDVTask.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testDVTask.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testDVTask.getRetryCounter()).isEqualTo(DEFAULT_RETRY_COUNTER);
        assertThat(testDVTask.getErrorDesc()).isEqualTo(DEFAULT_ERROR_DESC);
        assertThat(testDVTask.getOperationSystem()).isEqualTo(DEFAULT_OPERATION_SYSTEM);
        assertThat(testDVTask.getBrowser()).isEqualTo(DEFAULT_BROWSER);
        assertThat(testDVTask.getResolutionWidth()).isEqualTo(DEFAULT_RESOLUTION_WIDTH);
        assertThat(testDVTask.getResolutionHeight()).isEqualTo(DEFAULT_RESOLUTION_HEIGHT);
    }

    @Test
    @Transactional
    void createDVTaskWithExistingId() throws Exception {
        // Create the DVTask with an existing ID
        dVTask.setId(1L);

        int databaseSizeBeforeCreate = dVTaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDVTaskMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDVTasks() throws Exception {
        // Initialize the database
        dVTaskRepository.saveAndFlush(dVTask);

        // Get all the dVTaskList
        restDVTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dVTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].retryCounter").value(hasItem(DEFAULT_RETRY_COUNTER)))
            .andExpect(jsonPath("$.[*].errorDesc").value(hasItem(DEFAULT_ERROR_DESC)))
            .andExpect(jsonPath("$.[*].operationSystem").value(hasItem(DEFAULT_OPERATION_SYSTEM.toString())))
            .andExpect(jsonPath("$.[*].browser").value(hasItem(DEFAULT_BROWSER.toString())))
            .andExpect(jsonPath("$.[*].resolutionWidth").value(hasItem(DEFAULT_RESOLUTION_WIDTH)))
            .andExpect(jsonPath("$.[*].resolutionHeight").value(hasItem(DEFAULT_RESOLUTION_HEIGHT)));
    }

    @Test
    @Transactional
    void getDVTask() throws Exception {
        // Initialize the database
        dVTaskRepository.saveAndFlush(dVTask);

        // Get the dVTask
        restDVTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, dVTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dVTask.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.retryCounter").value(DEFAULT_RETRY_COUNTER))
            .andExpect(jsonPath("$.errorDesc").value(DEFAULT_ERROR_DESC))
            .andExpect(jsonPath("$.operationSystem").value(DEFAULT_OPERATION_SYSTEM.toString()))
            .andExpect(jsonPath("$.browser").value(DEFAULT_BROWSER.toString()))
            .andExpect(jsonPath("$.resolutionWidth").value(DEFAULT_RESOLUTION_WIDTH))
            .andExpect(jsonPath("$.resolutionHeight").value(DEFAULT_RESOLUTION_HEIGHT));
    }

    @Test
    @Transactional
    void getNonExistingDVTask() throws Exception {
        // Get the dVTask
        restDVTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDVTask() throws Exception {
        // Initialize the database
        dVTaskRepository.saveAndFlush(dVTask);

        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();

        // Update the dVTask
        DVTask updatedDVTask = dVTaskRepository.findById(dVTask.getId()).get();
        // Disconnect from session so that the updates on updatedDVTask are not directly saved in db
        em.detach(updatedDVTask);
        updatedDVTask
            .uuid(UPDATED_UUID)
            .createTime(UPDATED_CREATE_TIME)
            .retryCounter(UPDATED_RETRY_COUNTER)
            .errorDesc(UPDATED_ERROR_DESC)
            .operationSystem(UPDATED_OPERATION_SYSTEM)
            .browser(UPDATED_BROWSER)
            .resolutionWidth(UPDATED_RESOLUTION_WIDTH)
            .resolutionHeight(UPDATED_RESOLUTION_HEIGHT);

        restDVTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDVTask.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDVTask))
            )
            .andExpect(status().isOk());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
        DVTask testDVTask = dVTaskList.get(dVTaskList.size() - 1);
        assertThat(testDVTask.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testDVTask.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDVTask.getRetryCounter()).isEqualTo(UPDATED_RETRY_COUNTER);
        assertThat(testDVTask.getErrorDesc()).isEqualTo(UPDATED_ERROR_DESC);
        assertThat(testDVTask.getOperationSystem()).isEqualTo(UPDATED_OPERATION_SYSTEM);
        assertThat(testDVTask.getBrowser()).isEqualTo(UPDATED_BROWSER);
        assertThat(testDVTask.getResolutionWidth()).isEqualTo(UPDATED_RESOLUTION_WIDTH);
        assertThat(testDVTask.getResolutionHeight()).isEqualTo(UPDATED_RESOLUTION_HEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingDVTask() throws Exception {
        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();
        dVTask.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDVTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dVTask.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dVTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDVTask() throws Exception {
        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();
        dVTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dVTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDVTask() throws Exception {
        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();
        dVTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVTaskMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVTask))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDVTaskWithPatch() throws Exception {
        // Initialize the database
        dVTaskRepository.saveAndFlush(dVTask);

        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();

        // Update the dVTask using partial update
        DVTask partialUpdatedDVTask = new DVTask();
        partialUpdatedDVTask.setId(dVTask.getId());

        partialUpdatedDVTask
            .uuid(UPDATED_UUID)
            .retryCounter(UPDATED_RETRY_COUNTER)
            .operationSystem(UPDATED_OPERATION_SYSTEM)
            .resolutionWidth(UPDATED_RESOLUTION_WIDTH)
            .resolutionHeight(UPDATED_RESOLUTION_HEIGHT);

        restDVTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDVTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDVTask))
            )
            .andExpect(status().isOk());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
        DVTask testDVTask = dVTaskList.get(dVTaskList.size() - 1);
        assertThat(testDVTask.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testDVTask.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testDVTask.getRetryCounter()).isEqualTo(UPDATED_RETRY_COUNTER);
        assertThat(testDVTask.getErrorDesc()).isEqualTo(DEFAULT_ERROR_DESC);
        assertThat(testDVTask.getOperationSystem()).isEqualTo(UPDATED_OPERATION_SYSTEM);
        assertThat(testDVTask.getBrowser()).isEqualTo(DEFAULT_BROWSER);
        assertThat(testDVTask.getResolutionWidth()).isEqualTo(UPDATED_RESOLUTION_WIDTH);
        assertThat(testDVTask.getResolutionHeight()).isEqualTo(UPDATED_RESOLUTION_HEIGHT);
    }

    @Test
    @Transactional
    void fullUpdateDVTaskWithPatch() throws Exception {
        // Initialize the database
        dVTaskRepository.saveAndFlush(dVTask);

        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();

        // Update the dVTask using partial update
        DVTask partialUpdatedDVTask = new DVTask();
        partialUpdatedDVTask.setId(dVTask.getId());

        partialUpdatedDVTask
            .uuid(UPDATED_UUID)
            .createTime(UPDATED_CREATE_TIME)
            .retryCounter(UPDATED_RETRY_COUNTER)
            .errorDesc(UPDATED_ERROR_DESC)
            .operationSystem(UPDATED_OPERATION_SYSTEM)
            .browser(UPDATED_BROWSER)
            .resolutionWidth(UPDATED_RESOLUTION_WIDTH)
            .resolutionHeight(UPDATED_RESOLUTION_HEIGHT);

        restDVTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDVTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDVTask))
            )
            .andExpect(status().isOk());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
        DVTask testDVTask = dVTaskList.get(dVTaskList.size() - 1);
        assertThat(testDVTask.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testDVTask.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDVTask.getRetryCounter()).isEqualTo(UPDATED_RETRY_COUNTER);
        assertThat(testDVTask.getErrorDesc()).isEqualTo(UPDATED_ERROR_DESC);
        assertThat(testDVTask.getOperationSystem()).isEqualTo(UPDATED_OPERATION_SYSTEM);
        assertThat(testDVTask.getBrowser()).isEqualTo(UPDATED_BROWSER);
        assertThat(testDVTask.getResolutionWidth()).isEqualTo(UPDATED_RESOLUTION_WIDTH);
        assertThat(testDVTask.getResolutionHeight()).isEqualTo(UPDATED_RESOLUTION_HEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingDVTask() throws Exception {
        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();
        dVTask.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDVTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dVTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDVTask() throws Exception {
        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();
        dVTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDVTask() throws Exception {
        int databaseSizeBeforeUpdate = dVTaskRepository.findAll().size();
        dVTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVTaskMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVTask))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DVTask in the database
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDVTask() throws Exception {
        // Initialize the database
        dVTaskRepository.saveAndFlush(dVTask);

        int databaseSizeBeforeDelete = dVTaskRepository.findAll().size();

        // Delete the dVTask
        restDVTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, dVTask.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DVTask> dVTaskList = dVTaskRepository.findAll();
        assertThat(dVTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
