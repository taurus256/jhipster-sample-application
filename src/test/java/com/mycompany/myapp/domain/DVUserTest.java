package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DVUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DVUser.class);
        DVUser dVUser1 = new DVUser();
        dVUser1.setId(1L);
        DVUser dVUser2 = new DVUser();
        dVUser2.setId(dVUser1.getId());
        assertThat(dVUser1).isEqualTo(dVUser2);
        dVUser2.setId(2L);
        assertThat(dVUser1).isNotEqualTo(dVUser2);
        dVUser1.setId(null);
        assertThat(dVUser1).isNotEqualTo(dVUser2);
    }
}
