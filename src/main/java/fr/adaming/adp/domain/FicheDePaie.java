package fr.adaming.adp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FicheDePaie.
 */
@Entity
@Table(name = "fiche_de_paie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FicheDePaie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "salaire_brut", nullable = false)
    private Float salaireBrut;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "datepaiement", nullable = false)
    private LocalDate datepaiement;

    @NotNull
    @Column(name = "salaire_net", nullable = false)
    private Float salaireNet;

    @NotNull
    @Column(name = "montant_net_avant_impots", nullable = false)
    private Float montantNetAvantImpots;

    @NotNull
    @Column(name = "pro_fees", nullable = false)
    private Float proFees;

    @Column(name = "deductions")
    private Float deductions;

    @ManyToOne
    @JsonIgnoreProperties(value = { "conventionCollective", "employeur", "employee" }, allowSetters = true)
    private Contrat contrat;

    @ManyToOne
    @JsonIgnoreProperties(value = { "conventionCollectives", "locations" }, allowSetters = true)
    private Employeur employeur;

    @ManyToOne
    private TauxDImposition imposition;

    @ManyToMany
    @JoinTable(
        name = "rel_fiche_de_paie__cotisation",
        joinColumns = @JoinColumn(name = "fiche_de_paie_id"),
        inverseJoinColumns = @JoinColumn(name = "cotisation_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ficheDePaies" }, allowSetters = true)
    private Set<Cotisation> cotisations = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_fiche_de_paie__mention",
        joinColumns = @JoinColumn(name = "fiche_de_paie_id"),
        inverseJoinColumns = @JoinColumn(name = "mention_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ficheDePaies" }, allowSetters = true)
    private Set<Mention> mentions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FicheDePaie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getSalaireBrut() {
        return this.salaireBrut;
    }

    public FicheDePaie salaireBrut(Float salaireBrut) {
        this.setSalaireBrut(salaireBrut);
        return this;
    }

    public void setSalaireBrut(Float salaireBrut) {
        this.salaireBrut = salaireBrut;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public FicheDePaie startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public FicheDePaie endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getDatepaiement() {
        return this.datepaiement;
    }

    public FicheDePaie datepaiement(LocalDate datepaiement) {
        this.setDatepaiement(datepaiement);
        return this;
    }

    public void setDatepaiement(LocalDate datepaiement) {
        this.datepaiement = datepaiement;
    }

    public Float getSalaireNet() {
        return this.salaireNet;
    }

    public FicheDePaie salaireNet(Float salaireNet) {
        this.setSalaireNet(salaireNet);
        return this;
    }

    public void setSalaireNet(Float salaireNet) {
        this.salaireNet = salaireNet;
    }

    public Float getMontantNetAvantImpots() {
        return this.montantNetAvantImpots;
    }

    public FicheDePaie montantNetAvantImpots(Float montantNetAvantImpots) {
        this.setMontantNetAvantImpots(montantNetAvantImpots);
        return this;
    }

    public void setMontantNetAvantImpots(Float montantNetAvantImpots) {
        this.montantNetAvantImpots = montantNetAvantImpots;
    }

    public Float getProFees() {
        return this.proFees;
    }

    public FicheDePaie proFees(Float proFees) {
        this.setProFees(proFees);
        return this;
    }

    public void setProFees(Float proFees) {
        this.proFees = proFees;
    }

    public Float getDeductions() {
        return this.deductions;
    }

    public FicheDePaie deductions(Float deductions) {
        this.setDeductions(deductions);
        return this;
    }

    public void setDeductions(Float deductions) {
        this.deductions = deductions;
    }

    public Contrat getContrat() {
        return this.contrat;
    }

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }

    public FicheDePaie contrat(Contrat contrat) {
        this.setContrat(contrat);
        return this;
    }

    public Employeur getEmployeur() {
        return this.employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    public FicheDePaie employeur(Employeur employeur) {
        this.setEmployeur(employeur);
        return this;
    }

    public TauxDImposition getImposition() {
        return this.imposition;
    }

    public void setImposition(TauxDImposition tauxDImposition) {
        this.imposition = tauxDImposition;
    }

    public FicheDePaie imposition(TauxDImposition tauxDImposition) {
        this.setImposition(tauxDImposition);
        return this;
    }

    public Set<Cotisation> getCotisations() {
        return this.cotisations;
    }

    public void setCotisations(Set<Cotisation> cotisations) {
        this.cotisations = cotisations;
    }

    public FicheDePaie cotisations(Set<Cotisation> cotisations) {
        this.setCotisations(cotisations);
        return this;
    }

    public FicheDePaie addCotisation(Cotisation cotisation) {
        this.cotisations.add(cotisation);
        cotisation.getFicheDePaies().add(this);
        return this;
    }

    public FicheDePaie removeCotisation(Cotisation cotisation) {
        this.cotisations.remove(cotisation);
        cotisation.getFicheDePaies().remove(this);
        return this;
    }

    public Set<Mention> getMentions() {
        return this.mentions;
    }

    public void setMentions(Set<Mention> mentions) {
        this.mentions = mentions;
    }

    public FicheDePaie mentions(Set<Mention> mentions) {
        this.setMentions(mentions);
        return this;
    }

    public FicheDePaie addMention(Mention mention) {
        this.mentions.add(mention);
        mention.getFicheDePaies().add(this);
        return this;
    }

    public FicheDePaie removeMention(Mention mention) {
        this.mentions.remove(mention);
        mention.getFicheDePaies().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FicheDePaie)) {
            return false;
        }
        return id != null && id.equals(((FicheDePaie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FicheDePaie{" +
            "id=" + getId() +
            ", salaireBrut=" + getSalaireBrut() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", datepaiement='" + getDatepaiement() + "'" +
            ", salaireNet=" + getSalaireNet() +
            ", montantNetAvantImpots=" + getMontantNetAvantImpots() +
            ", proFees=" + getProFees() +
            ", deductions=" + getDeductions() +
            "}";
    }
}
