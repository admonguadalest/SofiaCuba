package com.company.test1.entity.ciclos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "EVENTO")
@Entity(name = "test1_Evento")
public class Evento extends StandardEntity {
    private static final long serialVersionUID = -1144439246283644299L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CICLO_ID")
    protected Ciclo ciclo;

    @Column(name = "NOMBRE")
    protected String nombre;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA")
    protected Date fecha;

    @Column(name = "ARCHIVADO")
    protected Boolean archivado;

    @Column(name = "TIPO_EVENTO")
    protected Integer tipoEvento;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public Integer getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(Integer tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Boolean getArchivado() {
        return archivado;
    }

    public void setArchivado(Boolean archivado) {
        this.archivado = archivado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }
}