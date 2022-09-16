package fr.adaming.adp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.adaming.adp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TauxDImpositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TauxDImposition.class);
        TauxDImposition tauxDImposition1 = new TauxDImposition();
        tauxDImposition1.setId(1L);
        TauxDImposition tauxDImposition2 = new TauxDImposition();
        tauxDImposition2.setId(tauxDImposition1.getId());
        assertThat(tauxDImposition1).isEqualTo(tauxDImposition2);
        tauxDImposition2.setId(2L);
        assertThat(tauxDImposition1).isNotEqualTo(tauxDImposition2);
        tauxDImposition1.setId(null);
        assertThat(tauxDImposition1).isNotEqualTo(tauxDImposition2);
    }
}
