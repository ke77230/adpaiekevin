package fr.adaming.adp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mention.
 */
@Entity
@Table(name = "mention")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "mention", nullable = false)
    private String mention;

    @ManyToMany(mappedBy = "mentions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contrat", "employeur", "imposition", "cotisations", "mentions" }, allowSetters = true)
    private Set<FicheDePaie> ficheDePaies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMention() {
        return this.mention;
    }

    public Mention mention(String mention) {
        this.setMention(mention);
        return this;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public Set<FicheDePaie> getFicheDePaies() {
        return this.ficheDePaies;
    }

    public void setFicheDePaies(Set<FicheDePaie> ficheDePaies) {
        if (this.ficheDePaies != null) {
            this.ficheDePaies.forEach(i -> i.removeMention(this));
        }
        if (ficheDePaies != null) {
            ficheDePaies.forEach(i -> i.addMention(this));
        }
        this.ficheDePaies = ficheDePaies;
    }

    public Mention ficheDePaies(Set<FicheDePaie> ficheDePaies) {
        this.setFicheDePaies(ficheDePaies);
        return this;
    }

    public Mention addFicheDePaie(FicheDePaie ficheDePaie) {
        this.ficheDePaies.add(ficheDePaie);
        ficheDePaie.getMentions().add(this);
        return this;
    }

    public Mention removeFicheDePaie(FicheDePaie ficheDePaie) {
        this.ficheDePaies.remove(ficheDePaie);
        ficheDePaie.getMentions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mention)) {
            return false;
        }
        return id != null && id.equals(((Mention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mention{" +
            "id=" + getId() +
            ", mention='" + getMention() + "'" +
            "}";
    }
}
