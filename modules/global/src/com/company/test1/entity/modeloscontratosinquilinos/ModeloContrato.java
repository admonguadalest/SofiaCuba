package com.company.test1.entity.modeloscontratosinquilinos;

import com.company.test1.entity.enums.ModeloContratoInquilinoPropietario;
import com.company.test1.validations.modeloscontratosinquilinos.ModeloContratoBean;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Table(name = "MODELO_CONTRATO")
@Entity(name = "test1_ModeloContrato")
@ModeloContratoBean(groups = UiCrossFieldChecks.class)
public class ModeloContrato extends StandardEntity {
    private static final long serialVersionUID = -3074147364882907035L;

    @Length(message = "Longitud de cadena 5-100", min = 5, max = 100)
    @NotNull
    @Column(name = "NOMBRE_MODELO")
    protected String nombreModelo;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Size(message = "Al menos registrar una seccion", min = 1, groups = UiCrossFieldChecks.class)
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "modeloContrato")
    protected List<Seccion> secciones = new ArrayList<Seccion>();

    @Column(name = "INQUILINO_PROPIETARIO")
    protected String inquilinoPropietario;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public void setInquilinoPropietario(ModeloContratoInquilinoPropietario inquilinoPropietario) {
        this.inquilinoPropietario = inquilinoPropietario == null ? null : inquilinoPropietario.getId();
    }

    public ModeloContratoInquilinoPropietario getInquilinoPropietario() {
        return inquilinoPropietario == null ? null : ModeloContratoInquilinoPropietario.fromId(inquilinoPropietario);
    }

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    @Override
    public Object clone(){
        /**
         * Probe de usar la JavaDeepCloneLibrary, pero el super problema es que muchos threads
         * distintos acceden al objeto clonado y entonces obtengo
         * ConcurrentModificationException's. Despues de mucho pelear
         * me es mÃ¡s rapido clonarlo manualmente, a parte de ser mÃ¡s eficiente.
         * JavaDeepClone es buena en caso que el objeto no estÃ© gesetionado por un entitymanager.
         */
        ModeloContrato clon = new ModeloContrato();
        clon.setDescripcion(this.descripcion);
        clon.setNombreModelo(this.nombreModelo);
        List seccionesClon = new ArrayList();

        for (int i = 0; i < this.getSecciones().size(); i++) {
            Seccion s = this.getSecciones().get(i);
            Seccion s_clon = (Seccion) s.clone();
            s_clon.setModeloContrato(clon);

            s_clon.setNombre(s.getNombre()+ "_");
            seccionesClon.add(s_clon);
        }

        clon.setSecciones(seccionesClon);
        return clon;
    }
}