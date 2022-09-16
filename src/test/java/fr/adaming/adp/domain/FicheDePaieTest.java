package fr.adaming.adp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.adaming.adp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FicheDePaieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FicheDePaie.class);
        FicheDePaie ficheDePaie1 = new FicheDePaie();
        ficheDePaie1.setId(1L);
        FicheDePaie ficheDePaie2 = new FicheDePaie();
        ficheDePaie2.setId(ficheDePaie1.getId());
        assertThat(ficheDePaie1).isEqualTo(ficheDePaie2);
        ficheDePaie2.setId(2L);
        assertThat(ficheDePaie1).isNotEqualTo(ficheDePaie2);
        ficheDePaie1.setId(null);
        assertThat(ficheDePaie1).isNotEqualTo(ficheDePaie2);
    }
}
