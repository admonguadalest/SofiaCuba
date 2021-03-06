package com.company.test1.entity.documentosImputables;

import com.company.test1.entity.Persona;
import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@DiscriminatorValue("DP")
@Entity(name = "test1_DocumentoProveedor")
public class DocumentoProveedor extends DocumentoImputable {
    private static final long serialVersionUID = 307383108336555158L;

    @NotNull(message = "Asignar Proveedor")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID")
    protected Proveedor proveedor;

    @NotNull(message = "Registrar un Titular")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TITULAR_PERSONA_ID")
    protected Persona titular;

    public Persona getTitular() {
        return titular;
    }

    public void setTitular(Persona titular) {
        this.titular = titular;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }


}