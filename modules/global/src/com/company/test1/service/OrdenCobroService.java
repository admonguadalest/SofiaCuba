package com.company.test1.service;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.ordenescobro.RealizacionCobro;
import com.company.test1.entity.recibos.Recibo;

import java.util.Date;
import java.util.List;

public interface OrdenCobroService {
    String NAME = "test1_OrdenCobroService";

    String generaEndToEndIdParaOrdenCobro(OrdenCobro oc, ContratoInquilino ci);

    List<OrdenCobro> generaOrdenesCobroParaRecibos(List<Recibo> rbos, Date fechaAdeudo);

    void guardaRealizacionCobroDeOrdenesCobroRecibo(RealizacionCobro rc);

    RealizacionCobro generaRealizacionCobroParaOrdenes(List<OrdenCobro> oocc, Persona presentador, CuentaBancaria cuentaBancariaReceptora) throws Exception;

}