package com.company.test1.entity.validaciones;

import com.company.test1.entity.enums.ValidacionEstado;
import com.company.test1.entity.extroles.Propietario;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "VALIDACION")
@Entity(name = "test1_Validacion")
public class Validacion extends StandardEntity {
    private static final long serialVersionUID = 795250787370057105L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPIETARIO_ID")
    protected Propietario propietario;

    @Column(name = "ESTADO_VALIDACION")
    protected Integer estadoValidacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_APROBACION_RECHAZO")
    protected Date fechaAprobacionRechazo;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Date getFechaAprobacionRechazo() {
        return fechaAprobacionRechazo;
    }

    public void setFechaAprobacionRechazo(Date fechaAprobacionRechazo) {
        this.fechaAprobacionRechazo = fechaAprobacionRechazo;
    }

    public ValidacionEstado getEstadoValidacion() {
        return estadoValidacion == null ? null : ValidacionEstado.fromId(estadoValidacion);
    }

    public void setEstadoValidacion(ValidacionEstado estadoValidacion) {
        this.estadoValidacion = estadoValidacion == null ? null : estadoValidacion.getId();
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }
}