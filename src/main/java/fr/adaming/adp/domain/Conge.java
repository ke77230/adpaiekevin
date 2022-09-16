package fr.adaming.adp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.adaming.adp.domain.enumeration.Decision;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conge.
 */
@Entity
@Table(name = "conge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Conge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "holdate_start", nullable = false)
    private LocalDate holdateStart;

    @NotNull
    @Column(name = "holdate_end", nullable = false)
    private LocalDate holdateEnd;

    @NotNull
    @Column(name = "holdate_pay", nullable = false)
    private Float holdatePay;

    @Column(name = "nb_conge_acquis")
    private Float nbCongeAcquis;

    @NotNull
    @Column(name = "nb_conge_pris", nullable = false)
    private Float nbCongePris;

    @NotNull
    @Column(name = "date_demande", nullable = false)
    private LocalDate dateDemande;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false)
    private Decision decision;

    @Column(name = "date_reponse")
    private LocalDate dateReponse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "conventionCollective", "employeur", "employee" }, allowSetters = true)
    private Contrat contrat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Conge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getHoldateStart() {
        return this.holdateStart;
    }

    public Conge holdateStart(LocalDate holdateStart) {
        this.setHoldateStart(holdateStart);
        return this;
    }

    public void setHoldateStart(LocalDate holdateStart) {
        this.holdateStart = holdateStart;
    }

    public LocalDate getHoldateEnd() {
        return this.holdateEnd;
    }

    public Conge holdateEnd(LocalDate holdateEnd) {
        this.setHoldateEnd(holdateEnd);
        return this;
    }

    public void setHoldateEnd(LocalDate holdateEnd) {
        this.holdateEnd = holdateEnd;
    }

    public Float getHoldatePay() {
        return this.holdatePay;
    }

    public Conge holdatePay(Float holdatePay) {
        this.setHoldatePay(holdatePay);
        return this;
    }

    public void setHoldatePay(Float holdatePay) {
        this.holdatePay = holdatePay;
    }

    public Float getNbCongeAcquis() {
        return this.nbCongeAcquis;
    }

    public Conge nbCongeAcquis(Float nbCongeAcquis) {
        this.setNbCongeAcquis(nbCongeAcquis);
        return this;
    }

    public void setNbCongeAcquis(Float nbCongeAcquis) {
        this.nbCongeAcquis = nbCongeAcquis;
    }

    public Float getNbCongePris() {
        return this.nbCongePris;
    }

    public Conge nbCongePris(Float nbCongePris) {
        this.setNbCongePris(nbCongePris);
        return this;
    }

    public void setNbCongePris(Float nbCongePris) {
        this.nbCongePris = nbCongePris;
    }

    public LocalDate getDateDemande() {
        return this.dateDemande;
    }

    public Conge dateDemande(LocalDate dateDemande) {
        this.setDateDemande(dateDemande);
        return this;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Decision getDecision() {
        return this.decision;
    }

    public Conge decision(Decision decision) {
        this.setDecision(decision);
        return this;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public LocalDate getDateReponse() {
        return this.dateReponse;
    }

    public Conge dateReponse(LocalDate dateReponse) {
        this.setDateReponse(dateReponse);
        return this;
    }

    public void setDateReponse(LocalDate dateReponse) {
        this.dateReponse = dateReponse;
    }

    public Contrat getContrat() {
        return this.contrat;
    }

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }

    public Conge contrat(Contrat contrat) {
        this.setContrat(contrat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conge)) {
            return false;
        }
        return id != null && id.equals(((Conge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conge{" +
            "id=" + getId() +
            ", holdateStart='" + getHoldateStart() + "'" +
            ", holdateEnd='" + getHoldateEnd() + "'" +
            ", holdatePay=" + getHoldatePay() +
            ", nbCongeAcquis=" + getNbCongeAcquis() +
            ", nbCongePris=" + getNbCongePris() +
            ", dateDemande='" + getDateDemande() + "'" +
            ", decision='" + getDecision() + "'" +
            ", dateReponse='" + getDateReponse() + "'" +
            "}";
    }
}
