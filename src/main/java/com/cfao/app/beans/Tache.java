package com.cfao.app.beans;

// Generated Aug 29, 2017 3:29:44 AM by Hibernate Tools 4.3.1


import javafx.beans.property.*;
import javafx.collections.FXCollections;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

/**
 * Taches generated by hbm2java
 */
@Entity
@Table(name = "taches"
)
public class Tache implements java.io.Serializable {


    private IntegerProperty idtache = new SimpleIntegerProperty();
    private SimpleStringProperty libelle = new SimpleStringProperty();
    private ListProperty<Planification> planifications = new SimpleListProperty<>();
    //private ListProperty<PlanificationModele> planificationModeles = new SimpleListProperty<>();

    public Tache() {
    }


    public Tache(Integer idtache, String libelle) {
        this.setIdtache(idtache);
        this.libelle.set(libelle);
    }

    public Tache(String libelle, List<Planification> planifications) {
        this.libelle.set(libelle);
        this.planifications.set(FXCollections.observableArrayList(planifications));
    }

    @Id
    @GeneratedValue(strategy = AUTO)


    @Column(name = "IDTACHE", unique = true, nullable = false)
    public Integer getIdtache() {
        return this.idtache.get();
    }

    public void setIdtache(Integer idtache) {
        this.idtache.set(idtache);
    }


    @Column(name = "LIBELLE", nullable = false, length = 250)
    public String getLibelle() {
        return this.libelle.get();
    }

    public void setLibelle(String libelle) {
        this.libelle.set(libelle);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "planification_tache", joinColumns = {
            @JoinColumn(name = "TACHE", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "PLANIFICATION", nullable = false, updatable = false)})
    public List<Planification> getPlanifications() {
        return this.planifications.get();
    }

    public void setPlanifications(List<Planification> planifications) {
        if (planifications != null) {
            this.planifications.set(FXCollections.observableArrayList(planifications));
        }
    }

    /*@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "planification_modele_tache", joinColumns = {
            @JoinColumn(name = "TACHE", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "PLANIFICATIONMODELE", nullable = false, updatable = false)})
    public List<PlanificationModele> getPlanificationModeles() {
        return planificationModeles.get();
    }

    public void setPlanificationModeles(List<PlanificationModele> planificationModeles) {
        if (planificationModeles != null) {
            this.planificationModeles.set(FXCollections.observableArrayList(planificationModeles));
        }
    }*/

    public SimpleStringProperty libelleProperty() {
        return libelle;
    }

    @Override
    public String toString(){
        return this.getLibelle();
    }
}


