package com.company.test1.service.accessory;

import com.company.test1.entity.recibos.Recibo;

public class Alteracion{
    Recibo recibo = null;
    Recibo reciboAnterior = null;
    String resumen = "";

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public Recibo getReciboAnterior() {
        return reciboAnterior;
    }

    public void setReciboAnterior(Recibo reciboAnterior) {
        this.reciboAnterior = reciboAnterior;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

}
