package com.gofind.gofind.domain.locations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gofind.gofind.domain.enumeration.EtatPiece;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Piece.
 */
@Table("piece")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Piece implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    private String libelle;

    @Column("image")
    private String image;

    @Column("etat")
    private EtatPiece etat;

    @Column("prix")
    private Double prix;

    @Transient
    @JsonIgnoreProperties(value = { "pieces", "proprietaire", "locations" }, allowSetters = true)
    private Maison maison;

    @Transient
    @JsonIgnoreProperties(value = { "pieces", "maison" }, allowSetters = true)
    private Location location;

    @Column("maison_id")
    private Long maisonId;

    @Column("location_id")
    private Long locationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Piece id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Piece libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getImage() {
        return this.image;
    }

    public Piece image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public EtatPiece getEtat() {
        return this.etat;
    }

    public Piece etat(EtatPiece etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(EtatPiece etat) {
        this.etat = etat;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Piece prix(Double prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Maison getMaison() {
        return this.maison;
    }

    public void setMaison(Maison maison) {
        this.maison = maison;
        this.maisonId = maison != null ? maison.getId() : null;
    }

    public Piece maison(Maison maison) {
        this.setMaison(maison);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.locationId = location != null ? location.getId() : null;
    }

    public Piece location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Long getMaisonId() {
        return this.maisonId;
    }

    public void setMaisonId(Long maison) {
        this.maisonId = maison;
    }

    public Long getLocationId() {
        return this.locationId;
    }

    public void setLocationId(Long location) {
        this.locationId = location;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piece)) {
            return false;
        }
        return getId() != null && getId().equals(((Piece) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Piece{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", image='" + getImage() + "'" +
            ", etat='" + getEtat() + "'" +
            ", prix='" + getPrix() + "'" +
            "}";
    }
}
