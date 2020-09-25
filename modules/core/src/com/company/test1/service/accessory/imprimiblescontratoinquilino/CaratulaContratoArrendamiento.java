package com.company.test1.service.accessory.imprimiblescontratoinquilino;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.extroles.Propietario;

import java.util.Hashtable;



/**
 *
 * @author Carlos Conti
 */
public abstract class CaratulaContratoArrendamiento{



    ContratoInquilino contratoInquilino = null;
    String pathImageMargenIzquierdo = "/images/";
    String pathMaestro = "CaratulaContratoArrendamientoLocalComercial.jasper";
    String titulo = "";
    Hashtable parameters = new Hashtable();


    public CaratulaContratoArrendamiento(ContratoInquilino contratoInquilino) {

        this.contratoInquilino = contratoInquilino;
        Propietario p = this.contratoInquilino.getDepartamento().getPropietarioEfectivo();

        pathImageMargenIzquierdo = "";

    }

    public Hashtable getParameters() {
        return parameters;
    }

    public void setParameters(Hashtable parameters) {
        this.parameters = parameters;
    }







}