package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "TEST1_REPRESENTACION_LEGAL")
@Entity(name = "test1_RepresentacionLegal")
public class RepresentacionLegal extends StandardEntity {
    private static final long serialVersionUID = 3230288668936717872L;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PERSONA_ID")
    protected Persona persona;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PERSONA_REPRESENTANTE_ID")
    protected Persona persona_representante;

    @Column(name = "EN_CALIDAD_DE")
    protected String enCalidadDe;

    public void setEnCalidadDe(RepresentacionEnCalidadDe enCalidadDe) {
        this.enCalidadDe = enCalidadDe == null ? null : enCalidadDe.getId();
    }

    public RepresentacionEnCalidadDe getEnCalidadDe() {
        return enCalidadDe == null ? null : RepresentacionEnCalidadDe.fromId(enCalidadDe);
    }

    public Persona getPersona_representante() {
        return persona_representante;
    }

    public void setPersona_representante(Persona persona_representante) {
        this.persona_representante = persona_representante;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public static RepresentacionEnCalidadDe getTipoRepresentacion(Persona representado, Persona representante) throws Exception{
        List<RepresentacionLegal> l = representado.getRepresentacionesLegales();
        for (int i = 0; i < l.size(); i++) {
            RepresentacionLegal rl = l.get(i);
            if (rl.getPersona_representante().getId().compareTo(representante.getId())==0){
                return rl.getEnCalidadDe();
            }
        }
        throw new Exception("No se hallo relacion de representacion entre " + representado.getNombreCompleto() + " y " + representante.getNombreCompleto());
    }
}