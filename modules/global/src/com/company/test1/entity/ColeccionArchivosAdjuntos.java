package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "COLECCION_ARCHIVOS_ADJUNTOS")
@Entity(name = "test1_ColeccionArchivosAdjuntos")
public class ColeccionArchivosAdjuntos extends StandardEntity {
    private static final long serialVersionUID = 4001663496801076791L;


    @Column(name = "NOMBRE")
    protected String nombre = "(Nombre Coleccion)";

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "coleccionParent")
    protected List<ColeccionArchivosAdjuntos> colecciones = new ArrayList<ColeccionArchivosAdjuntos>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "coleccionArchivosAdjuntos")
    protected List<ArchivoAdjunto> archivos = new ArrayList<ArchivoAdjunto>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_PARENT_ID")
    protected ColeccionArchivosAdjuntos coleccionParent = null;

    public List<ColeccionArchivosAdjuntos> getColecciones() {
        return colecciones;
    }

    public void setColecciones(List<ColeccionArchivosAdjuntos> colecciones) {
        this.colecciones = colecciones;
    }

    public ColeccionArchivosAdjuntos getColeccionParent() {
        return coleccionParent;
    }

    public void setColeccionParent(ColeccionArchivosAdjuntos coleccionParent) {
        this.coleccionParent = coleccionParent;
    }

    public List<ArchivoAdjunto> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoAdjunto> archivos) {
        this.archivos = archivos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}