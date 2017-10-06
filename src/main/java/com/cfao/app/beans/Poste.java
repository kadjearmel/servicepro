package com.cfao.app.beans;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Postes generated by hbm2java
 */
@Entity
@Table(name="postes"
)
public class Poste implements java.io.Serializable {


    private SimpleIntegerProperty idposte = new SimpleIntegerProperty();
    private ObjectProperty<Personne> personne = new SimpleObjectProperty<>();
    private ObjectProperty<Societe> societe = new SimpleObjectProperty<>();
    private SimpleStringProperty titre = new SimpleStringProperty();
    private SimpleStringProperty intituleBulletin = new SimpleStringProperty();
    private ObjectProperty<Date> datedebut = new SimpleObjectProperty<>();
    private ObjectProperty<Date> datefin = new SimpleObjectProperty<>();

    public Poste() {
    }


    public Poste(Personne personne, String titre) {
        this.personne.set(personne);
        this.titre.set(titre);
    }
    public Poste(Personne personne, Societe societe, String titre, Date datedebut, Date datefin) {
        this.personne.set(personne);
        this.societe.set(societe);
        this.titre.set(titre);
        this.datedebut.set(datedebut);
        this.datefin.set(datefin);
    }

    @Id @GeneratedValue(strategy=IDENTITY)


    @Column(name="IDPOSTE", unique=true, nullable=false)
    public int getIdposte() {
        return this.idposte.get();
    }

    public void setIdposte(Integer idposte) {
        this.idposte.set(idposte);
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PERSONNE", nullable=false)
    public Personne getPersonne() {
        return this.personne.get();
    }

    public void setPersonne(Personne personne) {
        this.personne.set(personne);
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SOCIETE")
    public Societe getSociete() {
        return this.societe.get();
    }

    public void setSociete(Societe societe) {
        this.societe.set(societe);
    }


    @Column(name="TITRE", nullable=false, length=150)
    public String getTitre() {
        return this.titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DATEDEBUT", length=10)
    public Date getDatedebut() {
        return this.datedebut.get();
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut.set(datedebut);
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DATEFIN", length=10)
    public Date getDatefin() {
        return this.datefin.get();
    }

    public void setDatefin(Date datefin) {
        this.datefin.set(datefin);
    }


    @Column(name = "INTITULEBULLETIN", length = 80)
    public String getIntituleBulletin() {
        return this.intituleBulletin.get();
    }

    public void setIntituleBulletin(String intitule) {
        this.intituleBulletin.set(intitule);
    }

    public SimpleStringProperty titreProperty() {
        return this.titre;
    }
    public SimpleStringProperty intituleBulletinProperty() {
        return this.intituleBulletin;
    }


    public ObjectProperty<Societe> societe() {
        return this.societe;
    }
}


