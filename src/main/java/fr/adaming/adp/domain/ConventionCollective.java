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
 * A ConventionCollective.
 */
@Entity
@Table(name = "convention_collective")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConventionCollective implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idcc", nullable = false)
    private Integer idcc;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "position", nullable = false)
    private Float position;

    @NotNull
    @Column(name = "coefficient", nullable = false)
    private Integer coefficient;

    @NotNull
    @Column(name = "valeur_point", nullable = false)
    private Float valeurPoint;

    @NotNull
    @Column(name = "base_fixe", nullable = false)
    private Float baseFixe;

    @NotNull
    @Column(name = "salaire_minimaux", nullable = false)
    private Float salaireMinimaux;

    @ManyToMany(mappedBy = "conventionCollectives")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conventionCollectives", "locations" }, allowSetters = true)
    private Set<Employeur> employeurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConventionCollective id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdcc() {
        return this.idcc;
    }

    public ConventionCollective idcc(Integer idcc) {
        this.setIdcc(idcc);
        return this;
    }

    public void setIdcc(Integer idcc) {
        this.idcc = idcc;
    }

    public String getNom() {
        return this.nom;
    }

    public ConventionCollective nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Float getPosition() {
        return this.position;
    }

    public ConventionCollective position(Float position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(Float position) {
        this.position = position;
    }

    public Integer getCoefficient() {
        return this.coefficient;
    }

    public ConventionCollective coefficient(Integer coefficient) {
        this.setCoefficient(coefficient);
        return this;
    }

    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }

    public Float getValeurPoint() {
        return this.valeurPoint;
    }

    public ConventionCollective valeurPoint(Float valeurPoint) {
        this.setValeurPoint(valeurPoint);
        return this;
    }

    public void setValeurPoint(Float valeurPoint) {
        this.valeurPoint = valeurPoint;
    }

    public Float getBaseFixe() {
        return this.baseFixe;
    }

    public ConventionCollective baseFixe(Float baseFixe) {
        this.setBaseFixe(baseFixe);
        return this;
    }

    public void setBaseFixe(Float baseFixe) {
        this.baseFixe = baseFixe;
    }

    public Float getSalaireMinimaux() {
        return this.salaireMinimaux;
    }

    public ConventionCollective salaireMinimaux(Float salaireMinimaux) {
        this.setSalaireMinimaux(salaireMinimaux);
        return this;
    }

    public void setSalaireMinimaux(Float salaireMinimaux) {
        this.salaireMinimaux = salaireMinimaux;
    }

    public Set<Employeur> getEmployeurs() {
        return this.employeurs;
    }

    public void setEmployeurs(Set<Employeur> employeurs) {
        if (this.employeurs != null) {
            this.employeurs.forEach(i -> i.removeConventionCollective(this));
        }
        if (employeurs != null) {
            employeurs.forEach(i -> i.addConventionCollective(this));
        }
        this.employeurs = employeurs;
    }

    public ConventionCollective employeurs(Set<Employeur> employeurs) {
        this.setEmployeurs(employeurs);
        return this;
    }

    public ConventionCollective addEmployeur(Employeur employeur) {
        this.employeurs.add(employeur);
        employeur.getConventionCollectives().add(this);
        return this;
    }

    public ConventionCollective removeEmployeur(Employeur employeur) {
        this.employeurs.remove(employeur);
        employeur.getConventionCollectives().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConventionCollective)) {
            return false;
        }
        return id != null && id.equals(((ConventionCollective) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConventionCollective{" +
            "id=" + getId() +
            ", idcc=" + getIdcc() +
            ", nom='" + getNom() + "'" +
            ", position=" + getPosition() +
            ", coefficient=" + getCoefficient() +
            ", valeurPoint=" + getValeurPoint() +
            ", baseFixe=" + getBaseFixe() +
            ", salaireMinimaux=" + getSalaireMinimaux() +
            "}";
    }
}
