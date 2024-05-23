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
 * A Location.
 */
@Table("location")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("prix")
    private Float prix;

    @Transient
    @JsonIgnoreProperties(value = { "maison", "location" }, allowSetters = true)
    private Set<Piece> pieces = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "pieces", "proprietaire", "locations" }, allowSetters = true)
    private Maison maison;

    @Column("maison_id")
    private Long maisonId;

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

    public Float getPrix() {
        return this.prix;
    }

    public Location prix(Float prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Set<Piece> getPieces() {
        return this.pieces;
    }

    public void setPieces(Set<Piece> pieces) {
        if (this.pieces != null) {
            this.pieces.forEach(i -> i.setLocation(null));
        }
        if (pieces != null) {
            pieces.forEach(i -> i.setLocation(this));
        }
        this.pieces = pieces;
    }

    public Location pieces(Set<Piece> pieces) {
        this.setPieces(pieces);
        return this;
    }

    public Location addPieces(Piece piece) {
        this.pieces.add(piece);
        piece.setLocation(this);
        return this;
    }

    public Location removePieces(Piece piece) {
        this.pieces.remove(piece);
        piece.setLocation(null);
        return this;
    }

    public Maison getMaison() {
        return this.maison;
    }

    public void setMaison(Maison maison) {
        this.maison = maison;
        this.maisonId = maison != null ? maison.getId() : null;
    }

    public Location maison(Maison maison) {
        this.setMaison(maison);
        return this;
    }

    public Long getMaisonId() {
        return this.maisonId;
    }

    public void setMaisonId(Long maison) {
        this.maisonId = maison;
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
        return getId() != null && getId().equals(((Location) o).getId());
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
            ", prix=" + getPrix() +
            "}";
    }
}
