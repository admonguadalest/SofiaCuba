package com.company.test1.web.screens.notificaciones;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;

import java.util.Comparator;

public class ContratoInquilinoIdDepartamento implements Comparator<ContratoInquilino> {

    public int compare(ContratoInquilino ci1, ContratoInquilino ci2){
        return ci1.getDepartamento().getRm2id().compareTo(ci2.getDepartamento().getRm2id());
    }

}