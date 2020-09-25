package com.company.test1.service.accessory.imprimiblescontratoinquilino;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.*;
/**
 *
 * @author Carlos Conti
 */


public class ImplementacionSeccion implements java.io.Serializable{
    String nombreSeccion = "";
    List implementacionesClausulas = new ArrayList();

    public List getImplementacionesClausulas() {
        return implementacionesClausulas;
    }

    public void setImplementacionesClausulas(List implementacionesClausulas) {
        this.implementacionesClausulas = implementacionesClausulas;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

}