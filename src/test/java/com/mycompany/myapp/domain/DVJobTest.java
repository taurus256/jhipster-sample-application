package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DVJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DVJob.class);
        DVJob dVJob1 = new DVJob();
        dVJob1.setId(1L);
        DVJob dVJob2 = new DVJob();
        dVJob2.setId(dVJob1.getId());
        assertThat(dVJob1).isEqualTo(dVJob2);
        dVJob2.setId(2L);
        assertThat(dVJob1).isNotEqualTo(dVJob2);
        dVJob1.setId(null);
        assertThat(dVJob1).isNotEqualTo(dVJob2);
    }
}
