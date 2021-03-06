package com.cfao.app.beans;


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PersonneQcmId generated by hbm2java
 */
@Embeddable
public class PersonneQcmId implements java.io.Serializable {


    private int personne;
    private int qcm;

    public PersonneQcmId() {
    }

    public PersonneQcmId(int personne, int qcm) {
        this.personne = personne;
        this.qcm = qcm;
    }


    @Column(name = "PERSONNE", nullable = false)
    public int getPersonne() {
        return this.personne;
    }

    public void setPersonne(int personne) {
        this.personne = personne;
    }


    @Column(name = "QCM", nullable = false)
    public int getQcm() {
        return this.qcm;
    }

    public void setQcm(int qcm) {
        this.qcm = qcm;
    }


    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof PersonneQcmId)) return false;
        PersonneQcmId castOther = (PersonneQcmId) other;

        return (this.getPersonne() == castOther.getPersonne())
                && (this.getQcm() == castOther.getQcm());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getPersonne();
        result = 37 * result + this.getQcm();
        return result;
    }


    @Override
    public String toString() {
        return "PersonneQcmId{" +
                "personne=" + personne +
                ", qcm=" + qcm +
                '}';
    }
}


