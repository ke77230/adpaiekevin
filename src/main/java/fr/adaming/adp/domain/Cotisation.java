package fr.adaming.adp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.adaming.adp.domain.enumeration.Categorie;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cotisation.
 */
@Entity
@Table(name = "cotisation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cotisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "famille", nullable = false)
    private Categorie famille;

    @NotNull
    @Column(name = "taux", nullable = false)
    private Float taux;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "actuel", nullable = false)
    private Boolean actuel;

    @ManyToMany(mappedBy = "cotisations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contrat", "employeur", "imposition", "cotisations", "mentions" }, allowSetters = true)
    private Set<FicheDePaie> ficheDePaies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cotisation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cotisation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Categorie getFamille() {
        return this.famille;
    }

    public Cotisation famille(Categorie famille) {
        this.setFamille(famille);
        return this;
    }

    public void setFamille(Categorie famille) {
        this.famille = famille;
    }

    public Float getTaux() {
        return this.taux;
    }

    public Cotisation taux(Float taux) {
        this.setTaux(taux);
        return this;
    }

    public void setTaux(Float taux) {
        this.taux = taux;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Cotisation startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Cotisation endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActuel() {
        return this.actuel;
    }

    public Cotisation actuel(Boolean actuel) {
        this.setActuel(actuel);
        return this;
    }

    public void setActuel(Boolean actuel) {
        this.actuel = actuel;
    }

    public Set<FicheDePaie> getFicheDePaies() {
        return this.ficheDePaies;
    }

    public void setFicheDePaies(Set<FicheDePaie> ficheDePaies) {
        if (this.ficheDePaies != null) {
            this.ficheDePaies.forEach(i -> i.removeCotisation(this));
        }
        if (ficheDePaies != null) {
            ficheDePaies.forEach(i -> i.addCotisation(this));
        }
        this.ficheDePaies = ficheDePaies;
    }

    public Cotisation ficheDePaies(Set<FicheDePaie> ficheDePaies) {
        this.setFicheDePaies(ficheDePaies);
        return this;
    }

    public Cotisation addFicheDePaie(FicheDePaie ficheDePaie) {
        this.ficheDePaies.add(ficheDePaie);
        ficheDePaie.getCotisations().add(this);
        return this;
    }

    public Cotisation removeFicheDePaie(FicheDePaie ficheDePaie) {
        this.ficheDePaies.remove(ficheDePaie);
        ficheDePaie.getCotisations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cotisation)) {
            return false;
        }
        return id != null && id.equals(((Cotisation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cotisation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", famille='" + getFamille() + "'" +
            ", taux=" + getTaux() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", actuel='" + getActuel() + "'" +
            "}";
    }
}
