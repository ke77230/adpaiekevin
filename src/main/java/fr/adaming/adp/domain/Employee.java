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
 * The Employee entity.
 */
@Schema(description = "The Employee entity.")
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Schema(description = "The firstname attribute.", required = true)
    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Size(min = 15, max = 15)
    @Column(name = "numero_securite_sociale", length = 15, nullable = false)
    private String numeroSecuriteSociale;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "taux_imposition")
    private Float tauxImposition;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employee", "jobs", "locations" }, allowSetters = true)
    private Employee employee;

    @ManyToMany
    @JoinTable(
        name = "rel_employee__job",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    @ManyToMany(mappedBy = "employees")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employees", "employeurs" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumeroSecuriteSociale() {
        return this.numeroSecuriteSociale;
    }

    public Employee numeroSecuriteSociale(String numeroSecuriteSociale) {
        this.setNumeroSecuriteSociale(numeroSecuriteSociale);
        return this;
    }

    public void setNumeroSecuriteSociale(String numeroSecuriteSociale) {
        this.numeroSecuriteSociale = numeroSecuriteSociale;
    }

    public String getQualification() {
        return this.qualification;
    }

    public Employee qualification(String qualification) {
        this.setQualification(qualification);
        return this;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Float getTauxImposition() {
        return this.tauxImposition;
    }

    public Employee tauxImposition(Float tauxImposition) {
        this.setTauxImposition(tauxImposition);
        return this;
    }

    public void setTauxImposition(Float tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public Employee jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Employee addJob(Job job) {
        this.jobs.add(job);
        job.getEmployees().add(this);
        return this;
    }

    public Employee removeJob(Job job) {
        this.jobs.remove(job);
        job.getEmployees().remove(this);
        return this;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        if (this.locations != null) {
            this.locations.forEach(i -> i.removeEmployee(this));
        }
        if (locations != null) {
            locations.forEach(i -> i.addEmployee(this));
        }
        this.locations = locations;
    }

    public Employee locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Employee addLocation(Location location) {
        this.locations.add(location);
        location.getEmployees().add(this);
        return this;
    }

    public Employee removeLocation(Location location) {
        this.locations.remove(location);
        location.getEmployees().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", numeroSecuriteSociale='" + getNumeroSecuriteSociale() + "'" +
            ", qualification='" + getQualification() + "'" +
            ", tauxImposition=" + getTauxImposition() +
            "}";
    }
}
