package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DVTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DVTask.class);
        DVTask dVTask1 = new DVTask();
        dVTask1.setId(1L);
        DVTask dVTask2 = new DVTask();
        dVTask2.setId(dVTask1.getId());
        assertThat(dVTask1).isEqualTo(dVTask2);
        dVTask2.setId(2L);
        assertThat(dVTask1).isNotEqualTo(dVTask2);
        dVTask1.setId(null);
        assertThat(dVTask1).isNotEqualTo(dVTask2);
    }
}
