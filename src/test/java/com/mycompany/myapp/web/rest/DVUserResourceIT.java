package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DVUser;
import com.mycompany.myapp.repository.DVUserRepository;
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
 * Integration tests for the {@link DVUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DVUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dv-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DVUserRepository dVUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDVUserMockMvc;

    private DVUser dVUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DVUser createEntity(EntityManager em) {
        DVUser dVUser = new DVUser().name(DEFAULT_NAME).email(DEFAULT_EMAIL);
        return dVUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DVUser createUpdatedEntity(EntityManager em) {
        DVUser dVUser = new DVUser().name(UPDATED_NAME).email(UPDATED_EMAIL);
        return dVUser;
    }

    @BeforeEach
    public void initTest() {
        dVUser = createEntity(em);
    }

    @Test
    @Transactional
    void createDVUser() throws Exception {
        int databaseSizeBeforeCreate = dVUserRepository.findAll().size();
        // Create the DVUser
        restDVUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVUser))
            )
            .andExpect(status().isCreated());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeCreate + 1);
        DVUser testDVUser = dVUserList.get(dVUserList.size() - 1);
        assertThat(testDVUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDVUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createDVUserWithExistingId() throws Exception {
        // Create the DVUser with an existing ID
        dVUser.setId(1L);

        int databaseSizeBeforeCreate = dVUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDVUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDVUsers() throws Exception {
        // Initialize the database
        dVUserRepository.saveAndFlush(dVUser);

        // Get all the dVUserList
        restDVUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dVUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getDVUser() throws Exception {
        // Initialize the database
        dVUserRepository.saveAndFlush(dVUser);

        // Get the dVUser
        restDVUserMockMvc
            .perform(get(ENTITY_API_URL_ID, dVUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dVUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingDVUser() throws Exception {
        // Get the dVUser
        restDVUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDVUser() throws Exception {
        // Initialize the database
        dVUserRepository.saveAndFlush(dVUser);

        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();

        // Update the dVUser
        DVUser updatedDVUser = dVUserRepository.findById(dVUser.getId()).get();
        // Disconnect from session so that the updates on updatedDVUser are not directly saved in db
        em.detach(updatedDVUser);
        updatedDVUser.name(UPDATED_NAME).email(UPDATED_EMAIL);

        restDVUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDVUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDVUser))
            )
            .andExpect(status().isOk());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
        DVUser testDVUser = dVUserList.get(dVUserList.size() - 1);
        assertThat(testDVUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDVUser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingDVUser() throws Exception {
        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();
        dVUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDVUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dVUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dVUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDVUser() throws Exception {
        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();
        dVUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dVUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDVUser() throws Exception {
        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();
        dVUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVUserMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dVUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDVUserWithPatch() throws Exception {
        // Initialize the database
        dVUserRepository.saveAndFlush(dVUser);

        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();

        // Update the dVUser using partial update
        DVUser partialUpdatedDVUser = new DVUser();
        partialUpdatedDVUser.setId(dVUser.getId());

        partialUpdatedDVUser.name(UPDATED_NAME);

        restDVUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDVUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDVUser))
            )
            .andExpect(status().isOk());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
        DVUser testDVUser = dVUserList.get(dVUserList.size() - 1);
        assertThat(testDVUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDVUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateDVUserWithPatch() throws Exception {
        // Initialize the database
        dVUserRepository.saveAndFlush(dVUser);

        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();

        // Update the dVUser using partial update
        DVUser partialUpdatedDVUser = new DVUser();
        partialUpdatedDVUser.setId(dVUser.getId());

        partialUpdatedDVUser.name(UPDATED_NAME).email(UPDATED_EMAIL);

        restDVUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDVUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDVUser))
            )
            .andExpect(status().isOk());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
        DVUser testDVUser = dVUserList.get(dVUserList.size() - 1);
        assertThat(testDVUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDVUser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingDVUser() throws Exception {
        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();
        dVUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDVUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dVUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDVUser() throws Exception {
        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();
        dVUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDVUser() throws Exception {
        int databaseSizeBeforeUpdate = dVUserRepository.findAll().size();
        dVUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDVUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dVUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DVUser in the database
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDVUser() throws Exception {
        // Initialize the database
        dVUserRepository.saveAndFlush(dVUser);

        int databaseSizeBeforeDelete = dVUserRepository.findAll().size();

        // Delete the dVUser
        restDVUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, dVUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DVUser> dVUserList = dVUserRepository.findAll();
        assertThat(dVUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
