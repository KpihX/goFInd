package com.gofind.gofind.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Utilisateur.
 */
@Table("utilisateur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("telephone")
    private String telephone;

    @Transient
    private User login;

    @Transient
    @JsonIgnoreProperties(value = { "proprietaire", "signalant" }, allowSetters = true)
    private Set<Objet> objects = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "proprietaire", "signalant" }, allowSetters = true)
    private Set<Objet> objectsSignales = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "proprietaire", "engages" }, allowSetters = true)
    private Set<Trajet> trajetsProps = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "pieces", "proprietaire", "locations" }, allowSetters = true)
    private Set<Maison> maisons = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "proprietaire", "engages" }, allowSetters = true)
    private Set<Trajet> trajets = new HashSet<>();

    @Column("login_id")
    private Long loginId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Utilisateur telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getLogin() {
        return this.login;
    }

    public void setLogin(User user) {
        this.login = user;
        this.loginId = user != null ? user.getId() : null;
    }

    public Utilisateur login(User user) {
        this.setLogin(user);
        return this;
    }

    public Set<Objet> getObjects() {
        return this.objects;
    }

    public void setObjects(Set<Objet> objets) {
        if (this.objects != null) {
            this.objects.forEach(i -> i.setProprietaire(null));
        }
        if (objets != null) {
            objets.forEach(i -> i.setProprietaire(this));
        }
        this.objects = objets;
    }

    public Utilisateur objects(Set<Objet> objets) {
        this.setObjects(objets);
        return this;
    }

    public Utilisateur addObjects(Objet objet) {
        this.objects.add(objet);
        objet.setProprietaire(this);
        return this;
    }

    public Utilisateur removeObjects(Objet objet) {
        this.objects.remove(objet);
        objet.setProprietaire(null);
        return this;
    }

    public Set<Objet> getObjectsSignales() {
        return this.objectsSignales;
    }

    public void setObjectsSignales(Set<Objet> objets) {
        if (this.objectsSignales != null) {
            this.objectsSignales.forEach(i -> i.setSignalant(null));
        }
        if (objets != null) {
            objets.forEach(i -> i.setSignalant(this));
        }
        this.objectsSignales = objets;
    }

    public Utilisateur objectsSignales(Set<Objet> objets) {
        this.setObjectsSignales(objets);
        return this;
    }

    public Utilisateur addObjectsSignales(Objet objet) {
        this.objectsSignales.add(objet);
        objet.setSignalant(this);
        return this;
    }

    public Utilisateur removeObjectsSignales(Objet objet) {
        this.objectsSignales.remove(objet);
        objet.setSignalant(null);
        return this;
    }

    public Set<Trajet> getTrajetsProps() {
        return this.trajetsProps;
    }

    public void setTrajetsProps(Set<Trajet> trajets) {
        if (this.trajetsProps != null) {
            this.trajetsProps.forEach(i -> i.setProprietaire(null));
        }
        if (trajets != null) {
            trajets.forEach(i -> i.setProprietaire(this));
        }
        this.trajetsProps = trajets;
    }

    public Utilisateur trajetsProps(Set<Trajet> trajets) {
        this.setTrajetsProps(trajets);
        return this;
    }

    public Utilisateur addTrajetsProp(Trajet trajet) {
        this.trajetsProps.add(trajet);
        trajet.setProprietaire(this);
        return this;
    }

    public Utilisateur removeTrajetsProp(Trajet trajet) {
        this.trajetsProps.remove(trajet);
        trajet.setProprietaire(null);
        return this;
    }

    public Set<Maison> getMaisons() {
        return this.maisons;
    }

    public void setMaisons(Set<Maison> maisons) {
        if (this.maisons != null) {
            this.maisons.forEach(i -> i.setProprietaire(null));
        }
        if (maisons != null) {
            maisons.forEach(i -> i.setProprietaire(this));
        }
        this.maisons = maisons;
    }

    public Utilisateur maisons(Set<Maison> maisons) {
        this.setMaisons(maisons);
        return this;
    }

    public Utilisateur addMaisons(Maison maison) {
        this.maisons.add(maison);
        maison.setProprietaire(this);
        return this;
    }

    public Utilisateur removeMaisons(Maison maison) {
        this.maisons.remove(maison);
        maison.setProprietaire(null);
        return this;
    }

    public Set<Trajet> getTrajets() {
        return this.trajets;
    }

    public void setTrajets(Set<Trajet> trajets) {
        this.trajets = trajets;
    }

    public Utilisateur trajets(Set<Trajet> trajets) {
        this.setTrajets(trajets);
        return this;
    }

    public Utilisateur addTrajets(Trajet trajet) {
        this.trajets.add(trajet);
        return this;
    }

    public Utilisateur removeTrajets(Trajet trajet) {
        this.trajets.remove(trajet);
        return this;
    }

    public Long getLoginId() {
        return this.loginId;
    }

    public void setLoginId(Long user) {
        this.loginId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
