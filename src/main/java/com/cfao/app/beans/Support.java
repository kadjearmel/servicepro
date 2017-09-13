package com.cfao.app.beans;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Supports generated by hbm2java
 */
@Entity
@Table(name = "supports"
)
public class Support implements java.io.Serializable {
    private SimpleIntegerProperty idsupport = new SimpleIntegerProperty();
    private SimpleStringProperty code = new SimpleStringProperty();
    private SimpleStringProperty titre = new SimpleStringProperty();
    private SimpleStringProperty lien = new SimpleStringProperty();
    private ListProperty<SupportFormation> supportFormations = new SimpleListProperty<>();

    public Support() {
    }


    public Support(String code, String lien) {
        this.code.set(code);
        this.lien.set(lien);
    }

    public Support(String code, String titre, String lien, List<SupportFormation> supportFormations) {
        this.code.set(code);
        this.titre.set(titre);
        this.lien.set(lien);
        this.supportFormations.set(FXCollections.observableArrayList(supportFormations));
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "IDSUPPORT", unique = true, nullable = false)
    public Integer getIdsupport() {
        return this.idsupport.get();
    }

    public void setIdsupport(Integer idsupport) {
        this.idsupport.set(idsupport);
    }


    @Column(name = "CODE", nullable = false, length = 8)
    public String getCode() {
        return this.code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }


    @Column(name = "TITRE", length = 150)
    public String getTitre() {
        return this.titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }


    @Column(name = "LIEN", nullable = false, length = 250)
    public String getLien() {
        return this.lien.get();
    }

    public void setLien(String lien) {
        this.lien.set(lien);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "support", cascade = CascadeType.ALL)
    public List<SupportFormation> getSupportFormations() {
        return this.supportFormations;
    }

    public void setSupportFormations(List<SupportFormation> supportFormations) {
        if (supportFormations != null) {
            this.supportFormations.set(FXCollections.observableArrayList(supportFormations));
        }
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public SimpleStringProperty titreProperty() {
        return titre;
    }

    public SimpleStringProperty lienProperty() {
        return lien;
    }

    @Override
    public String toString() {
        return "Support{" + getIdsupport() +
                " code=" + code.get() +
                ", titre=" + titre.get() +
                ", lien=" + lien.get() +
                '}';
    }
}


