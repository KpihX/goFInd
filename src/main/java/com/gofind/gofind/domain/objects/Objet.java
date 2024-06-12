package com.gofind.gofind.domain.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gofind.gofind.domain.enumeration.EtatObjet;
import com.gofind.gofind.domain.enumeration.TypeObjet;
import com.gofind.gofind.domain.users.Utilisateur;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Objet.
 */
@Table("objet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Objet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    private String libelle;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("type")
    private TypeObjet type;

    @Column("image")
    private String image;

    @NotNull(message = "must not be null")
    @Column("identifiant")
    private String identifiant;

    @NotNull(message = "must not be null")
    @Column("etat")
    private EtatObjet etat;

    @Transient
    @JsonIgnoreProperties(value = { "login", "objects", "objectsSignales", "trajetsProps", "maisons", "trajets" }, allowSetters = true)
    private Utilisateur proprietaire;

    @Transient
    @JsonIgnoreProperties(value = { "login", "objects", "objectsSignales", "trajetsProps", "maisons", "trajets" }, allowSetters = true)
    private Utilisateur signalant;

    @Column("proprietaire_id")
    private Long proprietaireId;

    @Column("signalant_id")
    private Long signalantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Objet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Objet libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return this.description;
    }

    public Objet description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeObjet getType() {
        return this.type;
    }

    public Objet type(TypeObjet type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeObjet type) {
        this.type = type;
    }

    public String getImage() {
        return this.image;
    }

    public Objet image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdentifiant() {
        return this.identifiant;
    }

    public Objet identifiant(String identifiant) {
        this.setIdentifiant(identifiant);
        return this;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public EtatObjet getEtat() {
        return this.etat;
    }

    public Objet etat(EtatObjet etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(EtatObjet etat) {
        this.etat = etat;
    }

    public Utilisateur getProprietaire() {
        return this.proprietaire;
    }

    public void setProprietaire(Utilisateur utilisateur) {
        this.proprietaire = utilisateur;
        this.proprietaireId = utilisateur != null ? utilisateur.getId() : null;
    }

    public Objet proprietaire(Utilisateur utilisateur) {
        this.setProprietaire(utilisateur);
        return this;
    }

    public Utilisateur getSignalant() {
        return this.signalant;
    }

    public void setSignalant(Utilisateur utilisateur) {
        this.signalant = utilisateur;
        this.signalantId = utilisateur != null ? utilisateur.getId() : null;
    }

    public Objet signalant(Utilisateur utilisateur) {
        this.setSignalant(utilisateur);
        return this;
    }

    public Long getProprietaireId() {
        return this.proprietaireId;
    }

    public void setProprietaireId(Long utilisateur) {
        this.proprietaireId = utilisateur;
    }

    public Long getSignalantId() {
        return this.signalantId;
    }

    public void setSignalantId(Long utilisateur) {
        this.signalantId = utilisateur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Objet)) {
            return false;
        }
        return getId() != null && getId().equals(((Objet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Objet{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", image='" + getImage() + "'" +
            ", identifiant='" + getIdentifiant() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
