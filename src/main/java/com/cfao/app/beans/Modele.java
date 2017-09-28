package com.cfao.app.beans;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

/**
 * EtatFormation generated by hbm2java
 */
@Entity
@Table(name = "modeles"
)
public class Modele implements java.io.Serializable {


    private SimpleIntegerProperty idmodele = new SimpleIntegerProperty();
    private SimpleStringProperty libelle = new SimpleStringProperty();
    private ListProperty<Formation> formations = new SimpleListProperty<>();

    public Modele() {
    }


    public Modele(String libelle) {
        this.libelle.set(libelle);
    }

    public Modele(Integer idmodele) {
        this.idmodele.set(idmodele);
    }

    public Modele(String libelle, Set<Formation> formationses) {
        this.libelle.set(libelle);
        this.formations.set(formations);
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "IDMODELE", unique = true, nullable = false)
    public Integer getIdmodele() {
        return this.idmodele.get();
    }

    public void setIdmodele(Integer idetatformation) {
        this.idmodele.set(idetatformation);
    }


    @Column(name = "LIBELLE", nullable = false, length = 50)
    public String getLibelle() {
        return this.libelle.get();
    }

    public void setLibelle(String libelle) {
        this.libelle.set(libelle);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modele")
    public List<Formation> getFormations() {
        return this.formations;
    }

    public void setFormations(List<Formation> formations) {
        this.formations.set(FXCollections.observableArrayList(formations));
    }

    @Override
    public String toString() {
        return getLibelle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Modele that = (Modele) o;

        if (idmodele != null) {
            return this.getIdmodele().equals(that.getIdmodele());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = idmodele != null ? idmodele.hashCode() : 0;
        result = 31 * result + (libelle != null ? libelle.hashCode() : 0);
        return result;
    }

    public ListProperty<Formation> formationsProperty(){
        return formations;
    }
    public SimpleStringProperty libelleProperty() {
        return libelle;
    }
}


