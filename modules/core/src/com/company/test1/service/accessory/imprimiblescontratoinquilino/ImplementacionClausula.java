package com.company.test1.service.accessory.imprimiblescontratoinquilino;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Carlos Conti
 */

//Esta cláusula parece que no es de persistencia dado que no habia Id.
public class ImplementacionClausula implements java.io.Serializable{

    String ordinalClausula = "";
    String textoClausula = "";

    public String getOrdinalClausula() {
        return ordinalClausula;
    }

    public void setOrdinalClausula(String ordinalClausula) {
        this.ordinalClausula = ordinalClausula;
    }

    public String getTextoClausula() {
        return textoClausula;
    }

    public void setTextoClausula(String textoClausula) {
        this.textoClausula = textoClausula;
    }



}