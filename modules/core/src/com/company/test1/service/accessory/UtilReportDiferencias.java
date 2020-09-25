package com.company.test1.service.accessory;

import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.recibos.Recibo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlo
 */
public class UtilReportDiferencias {

    Ubicacion ubicacion = null;
    List<Recibo> altas = new ArrayList<Recibo>();
    List<Recibo> bajas = new ArrayList<Recibo>();
    List<Alteracion> alteraciones = new ArrayList<Alteracion>();
    Double totalPostCCAAAnterior = 0.0;
    Double totalIncrementos = 0.0;
    Double totalDecrementos = 0.0;
    Double totalPostCCAAProxEmision = 0.0;



    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Recibo> getAltas() {
        return altas;
    }

    public void setAltas(List<Recibo> altas) {
        this.altas = altas;
    }

    public List<Recibo> getBajas() {
        return bajas;
    }

    public void setBajas(List<Recibo> bajas) {
        this.bajas = bajas;
    }

    public List<Alteracion> getAlteraciones() {
        return alteraciones;
    }

    public void setAlteraciones(List<Alteracion> alteraciones) {
        this.alteraciones = alteraciones;
    }

    public Double getTotalPostCCAAAnterior() {
        return totalPostCCAAAnterior;
    }

    public void setTotalPostCCAAAnterior(Double totalPostCCAAAnterior) {
        this.totalPostCCAAAnterior = totalPostCCAAAnterior;
    }

    public Double getTotalIncrementos() {
        return totalIncrementos;
    }

    public void setTotalIncrementos(Double totalIncrementos) {
        this.totalIncrementos = totalIncrementos;
    }

    public Double getTotalDecrementos() {
        return totalDecrementos;
    }

    public void setTotalDecrementos(Double totalDecrementos) {
        this.totalDecrementos = totalDecrementos;
    }

    public Double getTotalPostCCAAProxEmision() {
        return totalPostCCAAProxEmision;
    }

    public void setTotalPostCCAAProxEmision(Double totalPostCCAAProxEmision) {
        this.totalPostCCAAProxEmision = totalPostCCAAProxEmision;
    }



}
