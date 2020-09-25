package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.modeloscontratosinquilinos.Seccion;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "TEST1_SECCION_DESCARTADA")
@Entity(name = "test1_SeccionDescartada")
public class SeccionDescartada extends StandardEntity {
    private static final long serialVersionUID = -6621432357349263552L;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @JoinColumn(name = "SECCION_ID")
    protected Seccion seccion;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPLEMENTACION_MODELO_ID")
    protected ImplementacionModelo implementacionModelo;

    public ImplementacionModelo getImplementacionModelo() {
        return implementacionModelo;
    }

    public void setImplementacionModelo(ImplementacionModelo implementacionModelo) {
        this.implementacionModelo = implementacionModelo;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }
}