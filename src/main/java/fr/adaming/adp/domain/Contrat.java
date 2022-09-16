package fr.adaming.adp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.adaming.adp.domain.enumeration.TypeForfait;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contrat.
 */
@Entity
@Table(name = "contrat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contrat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "salaire_base", nullable = false)
    private Float salaireBase;

    @NotNull
    @Column(name = "emploi", nullable = false)
    private String emploi;

    @NotNull
    @Column(name = "date_arrive", nullable = false)
    private LocalDate dateArrive;

    @NotNull
    @Column(name = "classification", nullable = false)
    private Float classification;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_forfait", nullable = false)
    private TypeForfait typeForfait;

    @Column(name = "nb_heure")
    private Float nbHeure;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employeurs" }, allowSetters = true)
    private ConventionCollective conventionCollective;

    @ManyToOne
    @JsonIgnoreProperties(value = { "conventionCollectives", "locations" }, allowSetters = true)
    private Employeur employeur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employee", "jobs", "locations" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contrat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getSalaireBase() {
        return this.salaireBase;
    }

    public Contrat salaireBase(Float salaireBase) {
        this.setSalaireBase(salaireBase);
        return this;
    }

    public void setSalaireBase(Float salaireBase) {
        this.salaireBase = salaireBase;
    }

    public String getEmploi() {
        return this.emploi;
    }

    public Contrat emploi(String emploi) {
        this.setEmploi(emploi);
        return this;
    }

    public void setEmploi(String emploi) {
        this.emploi = emploi;
    }

    public LocalDate getDateArrive() {
        return this.dateArrive;
    }

    public Contrat dateArrive(LocalDate dateArrive) {
        this.setDateArrive(dateArrive);
        return this;
    }

    public void setDateArrive(LocalDate dateArrive) {
        this.dateArrive = dateArrive;
    }

    public Float getClassification() {
        return this.classification;
    }

    public Contrat classification(Float classification) {
        this.setClassification(classification);
        return this;
    }

    public void setClassification(Float classification) {
        this.classification = classification;
    }

    public TypeForfait getTypeForfait() {
        return this.typeForfait;
    }

    public Contrat typeForfait(TypeForfait typeForfait) {
        this.setTypeForfait(typeForfait);
        return this;
    }

    public void setTypeForfait(TypeForfait typeForfait) {
        this.typeForfait = typeForfait;
    }

    public Float getNbHeure() {
        return this.nbHeure;
    }

    public Contrat nbHeure(Float nbHeure) {
        this.setNbHeure(nbHeure);
        return this;
    }

    public void setNbHeure(Float nbHeure) {
        this.nbHeure = nbHeure;
    }

    public ConventionCollective getConventionCollective() {
        return this.conventionCollective;
    }

    public void setConventionCollective(ConventionCollective conventionCollective) {
        this.conventionCollective = conventionCollective;
    }

    public Contrat conventionCollective(ConventionCollective conventionCollective) {
        this.setConventionCollective(conventionCollective);
        return this;
    }

    public Employeur getEmployeur() {
        return this.employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    public Contrat employeur(Employeur employeur) {
        this.setEmployeur(employeur);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Contrat employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contrat)) {
            return false;
        }
        return id != null && id.equals(((Contrat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contrat{" +
            "id=" + getId() +
            ", salaireBase=" + getSalaireBase() +
            ", emploi='" + getEmploi() + "'" +
            ", dateArrive='" + getDateArrive() + "'" +
            ", classification=" + getClassification() +
            ", typeForfait='" + getTypeForfait() + "'" +
            ", nbHeure=" + getNbHeure() +
            "}";
    }
}
