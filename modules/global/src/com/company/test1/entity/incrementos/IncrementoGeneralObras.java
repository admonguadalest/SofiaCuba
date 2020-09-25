package com.company.test1.entity.incrementos;

import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.IncrementoGeneralObrasModoReparticion;
import com.company.test1.entity.recibos.Concepto;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Entity(name = "test1_IncrementoGeneralObras")
public class IncrementoGeneralObras extends Incremento {
    private static final long serialVersionUID = -2721492412349317120L;

    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_COEFICIENTE_ID")
    protected TipoCoeficiente tipoCoeficiente;

    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCEPTO_ID")
    protected Concepto concepto;

    @Column(name = "VALOR_COEFICIENTE")
    protected Double valorCoeficiente;

    @Column(name = "IMPORTE_GLOGAL_INCREMENTO")
    protected Double importeGlogalIncremento;

    @OnDelete(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UBICACION_ID")
    protected Ubicacion ubicacion;

    @Column(name = "MODO_REPARTICION")
    protected Integer modoReparticion;

    public IncrementoGeneralObrasModoReparticion getModoReparticion() {
        return modoReparticion == null ? null : IncrementoGeneralObrasModoReparticion.fromId(modoReparticion);
    }

    public void setModoReparticion(IncrementoGeneralObrasModoReparticion modoReparticion) {
        this.modoReparticion = modoReparticion == null ? null : modoReparticion.getId();
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Double getImporteGlogalIncremento() {
        return importeGlogalIncremento;
    }

    public void setImporteGlogalIncremento(Double importeGlogalIncremento) {
        this.importeGlogalIncremento = importeGlogalIncremento;
    }

    public Double getValorCoeficiente() {
        return valorCoeficiente;
    }

    public void setValorCoeficiente(Double valorCoeficiente) {
        this.valorCoeficiente = valorCoeficiente;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public TipoCoeficiente getTipoCoeficiente() {
        return tipoCoeficiente;
    }

    public void setTipoCoeficiente(TipoCoeficiente tipoCoeficiente) {
        this.tipoCoeficiente = tipoCoeficiente;
    }
}