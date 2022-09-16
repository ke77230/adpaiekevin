package fr.adaming.adp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "street_name", nullable = false)
    private String streetName;

    @NotNull
    @Column(name = "numero_rue", nullable = false)
    private String numeroRue;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "state_province", nullable = false)
    private String stateProvince;

    @ManyToMany
    @JoinTable(
        name = "rel_location__employee",
        joinColumns = @JoinColumn(name = "location_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee", "jobs", "locations" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_location__employeur",
        joinColumns = @JoinColumn(name = "location_id"),
        inverseJoinColumns = @JoinColumn(name = "employeur_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conventionCollectives", "locations" }, allowSetters = true)
    private Set<Employeur> employeurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public Location streetName(String streetName) {
        this.setStreetName(streetName);
        return this;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getNumeroRue() {
        return this.numeroRue;
    }

    public Location numeroRue(String numeroRue) {
        this.setNumeroRue(numeroRue);
        return this;
    }

    public void setNumeroRue(String numeroRue) {
        this.numeroRue = numeroRue;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Location postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public Location city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return this.stateProvince;
    }

    public Location stateProvince(String stateProvince) {
        this.setStateProvince(stateProvince);
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Location employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Location addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getLocations().add(this);
        return this;
    }

    public Location removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getLocations().remove(this);
        return this;
    }

    public Set<Employeur> getEmployeurs() {
        return this.employeurs;
    }

    public void setEmployeurs(Set<Employeur> employeurs) {
        this.employeurs = employeurs;
    }

    public Location employeurs(Set<Employeur> employeurs) {
        this.setEmployeurs(employeurs);
        return this;
    }

    public Location addEmployeur(Employeur employeur) {
        this.employeurs.add(employeur);
        employeur.getLocations().add(this);
        return this;
    }

    public Location removeEmployeur(Employeur employeur) {
        this.employeurs.remove(employeur);
        employeur.getLocations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", streetName='" + getStreetName() + "'" +
            ", numeroRue='" + getNumeroRue() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            "}";
    }
}
