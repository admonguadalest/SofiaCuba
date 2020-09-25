package com.company.test1.entity.documentacionesinquilinos;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TEST1_DOCUMENTACION_INQUILINO")
@Entity(name = "test1_DocumentacionInquilino")
public class DocumentacionInquilino extends StandardEntity {
    private static final long serialVersionUID = -9030515828762246408L;

    @Column(name = "DNI")
    protected String dni;

    @Column(name = "NOMBRE_COMPLETO")
    protected String nombreCompleto;

    @Column(name = "OBJETO_CANIDADO")
    protected String objetoCanidado;

    @Column(name = "INFORMACION_DE_CONTACTO")
    protected String informacionDeContacto;

    @Lob
    @Column(name = "PRESENTACION")
    protected String presentacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_REGISTRO")
    protected Date fechaRegistro = new Date();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    protected ColeccionArchivosAdjuntos coleccionArchivosAdjuntos = null;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getInformacionDeContacto() {
        return informacionDeContacto;
    }

    public void setInformacionDeContacto(String informacionDeContacto) {
        this.informacionDeContacto = informacionDeContacto;
    }

    public String getObjetoCanidado() {
        return objetoCanidado;
    }

    public void setObjetoCanidado(String objetoCanidado) {
        this.objetoCanidado = objetoCanidado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}