package fr.adaming.adp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.adaming.adp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConventionCollectiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConventionCollective.class);
        ConventionCollective conventionCollective1 = new ConventionCollective();
        conventionCollective1.setId(1L);
        ConventionCollective conventionCollective2 = new ConventionCollective();
        conventionCollective2.setId(conventionCollective1.getId());
        assertThat(conventionCollective1).isEqualTo(conventionCollective2);
        conventionCollective2.setId(2L);
        assertThat(conventionCollective1).isNotEqualTo(conventionCollective2);
        conventionCollective1.setId(null);
        assertThat(conventionCollective1).isNotEqualTo(conventionCollective2);
    }
}
