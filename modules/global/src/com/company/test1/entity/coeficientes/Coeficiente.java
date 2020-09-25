package com.company.test1.entity.coeficientes;

import com.company.test1.entity.departamentos.Departamento;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "TEST1_COEFICIENTE")
@Entity(name = "test1_Coeficiente")
public class Coeficiente extends StandardEntity {
    private static final long serialVersionUID = -244565707969823474L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_COEFICIENTE_ID")
    protected TipoCoeficiente tipoCoeficiente;

    @Column(name = "VALOR")
    protected Double valor;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTAMENTO_ID")
    protected Departamento departamento;

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TipoCoeficiente getTipoCoeficiente() {
        return tipoCoeficiente;
    }

    public void setTipoCoeficiente(TipoCoeficiente tipoCoeficiente) {
        this.tipoCoeficiente = tipoCoeficiente;
    }
}