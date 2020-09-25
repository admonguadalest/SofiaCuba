package com.company.test1.entity.coeficientes;

import com.company.test1.entity.departamentos.Ubicacion;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "TEST1_UBICACION_COEFICIENTE")
@Entity(name = "test1_UbicacionCoeficiente")
public class UbicacionCoeficiente extends StandardEntity {
    private static final long serialVersionUID = 7051874995493817962L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UBICACION_ID")
    protected Ubicacion ubicacion;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_COEFICIENTE_ID")
    protected TipoCoeficiente tipoCoeficiente;

    @Column(name = "TOTAL_TIPO_COEFICIENTE")
    protected Double totalTipoCoeficiente;

    public Double getTotalTipoCoeficiente() {
        return totalTipoCoeficiente;
    }

    public void setTotalTipoCoeficiente(Double totalTipoCoeficiente) {
        this.totalTipoCoeficiente = totalTipoCoeficiente;
    }

    public TipoCoeficiente getTipoCoeficiente() {
        return tipoCoeficiente;
    }

    public void setTipoCoeficiente(TipoCoeficiente tipoCoeficiente) {
        this.tipoCoeficiente = tipoCoeficiente;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}