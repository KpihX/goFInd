package com.gofind.gofind.domain.locations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gofind.gofind.domain.users.Utilisateur;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Maison.
 */
@Table("maison")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Maison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("adresse")
    private String adresse;

    @Column("description")
    private String description;

    @Column("image")
    private String image;

    @Transient
    @JsonIgnoreProperties(value = { "maison", "location" }, allowSetters = true)
    private Set<Piece> pieces = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "login", "objects", "objectsSignales", "trajetsProps", "maisons", "trajets" }, allowSetters = true)
    private Utilisateur proprietaire;

    @Transient
    @JsonIgnoreProperties(value = { "pieces", "maison" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    @Column("proprietaire_id")
    private Long proprietaireId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Maison id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Maison adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return this.description;
    }

    public Maison description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public Maison image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Piece> getPieces() {
        return this.pieces;
    }

    public void setPieces(Set<Piece> pieces) {
        if (this.pieces != null) {
            this.pieces.forEach(i -> i.setMaison(null));
        }
        if (pieces != null) {
            pieces.forEach(i -> i.setMaison(this));
        }
        this.pieces = pieces;
    }

    public Maison pieces(Set<Piece> pieces) {
        this.setPieces(pieces);
        return this;
    }

    public Maison addPieces(Piece piece) {
        this.pieces.add(piece);
        piece.setMaison(this);
        return this;
    }

    public Maison removePieces(Piece piece) {
        this.pieces.remove(piece);
        piece.setMaison(null);
        return this;
    }

    public Utilisateur getProprietaire() {
        return this.proprietaire;
    }

    public void setProprietaire(Utilisateur utilisateur) {
        this.proprietaire = utilisateur;
        this.proprietaireId = utilisateur != null ? utilisateur.getId() : null;
    }

    public Maison proprietaire(Utilisateur utilisateur) {
        this.setProprietaire(utilisateur);
        return this;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        if (this.locations != null) {
            this.locations.forEach(i -> i.setMaison(null));
        }
        if (locations != null) {
            locations.forEach(i -> i.setMaison(this));
        }
        this.locations = locations;
    }

    public Maison locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Maison addLocations(Location location) {
        this.locations.add(location);
        location.setMaison(this);
        return this;
    }

    public Maison removeLocations(Location location) {
        this.locations.remove(location);
        location.setMaison(null);
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
        if (!(o instanceof Maison)) {
            return false;
        }
        return getId() != null && getId().equals(((Maison) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Maison{" +
            "id=" + getId() +
            ", adresse='" + getAdresse() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
