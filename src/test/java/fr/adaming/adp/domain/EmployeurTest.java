package fr.adaming.adp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.adaming.adp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employeur.class);
        Employeur employeur1 = new Employeur();
        employeur1.setId(1L);
        Employeur employeur2 = new Employeur();
        employeur2.setId(employeur1.getId());
        assertThat(employeur1).isEqualTo(employeur2);
        employeur2.setId(2L);
        assertThat(employeur1).isNotEqualTo(employeur2);
        employeur1.setId(null);
        assertThat(employeur1).isNotEqualTo(employeur2);
    }
}
