package com.cfao.app.beans;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CompetenceStatut generated by hbm2java
 */
@Entity
@Table(name="competence_statut"
        ,catalog="servicepro"
)
public class CompetenceStatut  implements java.io.Serializable {


    private SimpleStringProperty statut = new SimpleStringProperty();
    private SimpleStringProperty libelle = new SimpleStringProperty();
   // private Set<PersonneCompetence> personneCompetences = new HashSet<PersonneCompetence>(0);

    public CompetenceStatut() {
    }


    public CompetenceStatut(String statut) {
        this.statut.set(statut);
    }
    public CompetenceStatut(String statut, String libelle) {
        this.statut.set(statut);
        this.libelle.set(libelle);
    }

    @Id
    @Column(name="STATUT", unique=true, nullable=false, length=2)
    public String getStatut() {
        return this.statut.get();
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    /*@OneToMany(fetch=FetchType.LAZY, mappedBy="competenceStatut")
    public Set<PersonneCompetence> getPersonneCompetences() {
        return this.personneCompetences;
    }

    public void setPersonneCompetences(Set<PersonneCompetence> personneCompetences) {
        this.personneCompetences = personneCompetences;
    }

    */
    @Column(name = "LIBELLE", nullable = false)
    public String getLibelle() {
        return libelle.get();
    }

    public SimpleStringProperty libelleProperty() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle.set(libelle);
    }

    @Override
    public String toString() {
        return getLibelle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompetenceStatut that = (CompetenceStatut) o;

        if(statut != null){
            return statut.equals(that.getStatut());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = statut != null ? statut.hashCode() : 0;
        result = 31 * result + (libelle != null ? libelle.hashCode() : 0);
        return result;
    }
}


