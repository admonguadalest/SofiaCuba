package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TEST1_REGISTRO_PRESENCIAL")
@Entity(name = "test1_RegistroPresencial")
public class RegistroPresencial extends StandardEntity {
    private static final long serialVersionUID = 934903211849923908L;

    @Lookup(type = LookupType.SCREEN)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID")
    protected User usuario;

    @Temporal(TemporalType.TIME)
    @NotNull
    @Column(name = "TIMESTAMP", nullable = false)
    protected Date timestamp;

    @NotNull
    @Column(name = "ACCION", nullable = false)
    protected String accion;

    @Column(name = "UBICACION")
    protected String ubicacion;

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}