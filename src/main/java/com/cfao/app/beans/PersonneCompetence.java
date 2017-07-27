package com.cfao.app.beans;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PersonneCompetence generated by hbm2java
 */
@Entity
@Table(name="personne_competence"
        ,catalog="servicepro"
)
public class PersonneCompetence  implements java.io.Serializable {


    private PersonneCompetenceId id;
    private CompetenceStatut competenceStatut;
    private Competence competence;
    private Formation formation;
    private Personne personne;

    public PersonneCompetence() {
    }


    public PersonneCompetence(PersonneCompetenceId id, CompetenceStatut competenceStatut, Competence competence, Personne personne) {
        this.id = id;
        this.competenceStatut = competenceStatut;
        this.competence = competence;
        this.personne = personne;
    }
    public PersonneCompetence(PersonneCompetenceId id, CompetenceStatut competenceStatut, Competence competence, Formation formation,
                              Personne personne) {
        this.id = id;
        this.competenceStatut = competenceStatut;
        this.competence = competence;
        this.formation = formation;
        this.personne = personne;
    }

    @EmbeddedId


    @AttributeOverrides( {
            @AttributeOverride(name="personne", column=@Column(name="PERSONNE", nullable=false) ),
            @AttributeOverride(name="competence", column=@Column(name="COMPETENCE", nullable=false) ),
            @AttributeOverride(name="certification", column=@Column(name="CERTIFICATION", nullable=false, length=2) ),
            @AttributeOverride(name="formation", column=@Column(name="FORMATION") ) } )
    public PersonneCompetenceId getId() {
        return this.id;
    }

    public void setId(PersonneCompetenceId id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CERTIFICATION", nullable=false, insertable=false, updatable=false)
    public CompetenceStatut getCompetenceStatut() {
        return this.competenceStatut;
    }

    public void setCompetenceStatut(CompetenceStatut competenceStatut) {
        this.competenceStatut = competenceStatut;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="COMPETENCE", nullable=false, insertable=false, updatable=false)
    public Competence getCompetence() {
        return this.competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FORMATION", insertable=false, updatable=false)
    public Formation getFormation() {
        return this.formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PERSONNE", nullable=false, insertable=false, updatable=false)
    public Personne getPersonne() {
        return this.personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }




}
