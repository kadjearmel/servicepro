package com.cfao.app.beans;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;

/**
 * ProfilPersonne generated by hbm2java
 */
@Entity
@Table(name="profil_personne"
)
public class ProfilPersonne  implements java.io.Serializable {


    private ProfilPersonneId id = new ProfilPersonneId();
    private ObjectProperty<Niveau> niveau = new SimpleObjectProperty<>();
    private ObjectProperty<Personne> personne = new SimpleObjectProperty<>();
    private ObjectProperty<Profil> profil = new SimpleObjectProperty<>();

    public ProfilPersonne() {
    }

    public ProfilPersonne(ProfilPersonneId id, Niveau niveau, Personne personne, Profil profil) {
        this.id = id;
        this.niveau.set(niveau);
        this.personne.set(personne);
        this.profil.set(profil);
    }

    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name="personne", column=@Column(name="PERSONNE", nullable=false) ),
            @AttributeOverride(name="profil", column=@Column(name="PROFIL", nullable=false) ),
            @AttributeOverride(name="niveau", column=@Column(name="NIVEAU", nullable=false) ) } )
    public ProfilPersonneId getId() {
        return this.id;
    }

    public void setId(ProfilPersonneId id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NIVEAU", nullable=false, insertable=false, updatable=false)
    public Niveau getNiveau() {
        return this.niveau.get();
    }

    public void setNiveau(Niveau niveau) {
        this.niveau.set(niveau);
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PERSONNE", nullable=false, insertable=false, updatable=false)
    public Personne getPersonne() {
        return this.personne.get();
    }

    public void setPersonne(Personne personne) {
        this.personne.set(personne);
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROFIL", nullable=false, insertable=false, updatable=false)
    public Profil getProfil() {
        return this.profil.get();
    }

    public void setProfil(Profil profil) {
        this.profil.set(profil);
    }


    public ObjectProperty<Profil> profil() {
        return this.profil;
    }

    public ObjectProperty<Niveau> niveau() {
        return this.niveau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfilPersonne that = (ProfilPersonne) o;

        if (id != null) {
            return id.equals(that.id);
        }return false;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;

        return result;
    }

    @Override
    public String toString() {
        return "ProfilPersonne{" +
                "niveau=" + niveau +
                ", personne=" + personne +
                ", profil=" + profil +
                '}';
    }
}


