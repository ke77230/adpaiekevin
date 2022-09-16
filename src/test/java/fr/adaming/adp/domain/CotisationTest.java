package fr.adaming.adp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.adaming.adp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CotisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cotisation.class);
        Cotisation cotisation1 = new Cotisation();
        cotisation1.setId(1L);
        Cotisation cotisation2 = new Cotisation();
        cotisation2.setId(cotisation1.getId());
        assertThat(cotisation1).isEqualTo(cotisation2);
        cotisation2.setId(2L);
        assertThat(cotisation1).isNotEqualTo(cotisation2);
        cotisation1.setId(null);
        assertThat(cotisation1).isNotEqualTo(cotisation2);
    }
}
