package fr.adaming.adp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.adaming.adp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mention.class);
        Mention mention1 = new Mention();
        mention1.setId(1L);
        Mention mention2 = new Mention();
        mention2.setId(mention1.getId());
        assertThat(mention1).isEqualTo(mention2);
        mention2.setId(2L);
        assertThat(mention1).isNotEqualTo(mention2);
        mention1.setId(null);
        assertThat(mention1).isNotEqualTo(mention2);
    }
}
