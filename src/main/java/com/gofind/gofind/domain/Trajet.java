package com.gofind.gofind.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Trajet.
 */
@Table("trajet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trajet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("depart")
    private String depart;

    @NotNull(message = "must not be null")
    @Column("arrivee")
    private String arrivee;

    @NotNull(message = "must not be null")
    @Column("date_heure_depart")
    private ZonedDateTime dateHeureDepart;

    @NotNull(message = "must not be null")
    @Column("places")
    private Integer places;

    @NotNull(message = "must not be null")
    @Column("prix")
    private Float prix;

    @Transient
    @JsonIgnoreProperties(value = { "login", "objects", "objectsSignales", "trajetsProps", "maisons", "trajets" }, allowSetters = true)
    private Utilisateur proprietaire;

    @Transient
    @JsonIgnoreProperties(value = { "login", "objects", "objectsSignales", "trajetsProps", "maisons", "trajets" }, allowSetters = true)
    private Set<Utilisateur> engages = new HashSet<>();

    @Column("proprietaire_id")
    private Long proprietaireId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trajet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepart() {
        return this.depart;
    }

    public Trajet depart(String depart) {
        this.setDepart(depart);
        return this;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrivee() {
        return this.arrivee;
    }

    public Trajet arrivee(String arrivee) {
        this.setArrivee(arrivee);
        return this;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public ZonedDateTime getDateHeureDepart() {
        return this.dateHeureDepart;
    }

    public Trajet dateHeureDepart(ZonedDateTime dateHeureDepart) {
        this.setDateHeureDepart(dateHeureDepart);
        return this;
    }

    public void setDateHeureDepart(ZonedDateTime dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }

    public Integer getPlaces() {
        return this.places;
    }

    public Trajet places(Integer places) {
        this.setPlaces(places);
        return this;
    }

    public void setPlaces(Integer places) {
        this.places = places;
    }

    public Float getPrix() {
        return this.prix;
    }

    public Trajet prix(Float prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Utilisateur getProprietaire() {
        return this.proprietaire;
    }

    public void setProprietaire(Utilisateur utilisateur) {
        this.proprietaire = utilisateur;
        this.proprietaireId = utilisateur != null ? utilisateur.getId() : null;
    }

    public Trajet proprietaire(Utilisateur utilisateur) {
        this.setProprietaire(utilisateur);
        return this;
    }

    public Set<Utilisateur> getEngages() {
        return this.engages;
    }

    public void setEngages(Set<Utilisateur> utilisateurs) {
        if (this.engages != null) {
            this.engages.forEach(i -> i.removeTrajets(this));
        }
        if (utilisateurs != null) {
            utilisateurs.forEach(i -> i.addTrajets(this));
        }
        this.engages = utilisateurs;
    }

    public Trajet engages(Set<Utilisateur> utilisateurs) {
        this.setEngages(utilisateurs);
        return this;
    }

    public Trajet addEngages(Utilisateur utilisateur) {
        this.engages.add(utilisateur);
        utilisateur.getTrajets().add(this);
        return this;
    }

    public Trajet removeEngages(Utilisateur utilisateur) {
        this.engages.remove(utilisateur);
        utilisateur.getTrajets().remove(this);
        return this;
    }

    public Long getProprietaireId() {
        return this.proprietaireId;
    }

    public void setProprietaireId(Long utilisateur) {
        this.proprietaireId = utilisateur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trajet)) {
            return false;
        }
        return getId() != null && getId().equals(((Trajet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trajet{" +
            "id=" + getId() +
            ", depart='" + getDepart() + "'" +
            ", arrivee='" + getArrivee() + "'" +
            ", dateHeureDepart='" + getDateHeureDepart() + "'" +
            ", places=" + getPlaces() +
            ", prix=" + getPrix() +
            "}";
    }
}
