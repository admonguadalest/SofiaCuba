package com.company.test1.entity.modeloscontratosinquilinos;

import com.company.test1.validations.modeloscontratosinquilinos.SeccionBean;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Table(name = "SECCION")
@Entity(name = "test1_Seccion")
@SeccionBean(groups = UiCrossFieldChecks.class)
public class Seccion extends StandardEntity {
    private static final long serialVersionUID = 9135684711145642879L;

    @NotNull(message = "La Seccion debe estar asociada a un Modelo de Contrato")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODELO_CONTRATO_ID")
    protected ModeloContrato modeloContrato;

    @Length(message = "El nombre de la seccion debera tener al menos 3 caracteres", min = 3)
    @NotNull(message = "Aportar nombre para seccion")
    @Column(name = "NOMBRE")
    protected String nombre;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "OBLIGATORIA")
    protected Boolean obligatoria;

    @Size(message = "Aportar al menos una clausula", min = 1)
    @NotNull
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "seccion")
    protected List<Clausula> clausulas = new ArrayList<Clausula>();

    @Positive(message = "Aportar un valor positivo al campo Ordenacion")
    @NotNull(message = "Aportar un numero a Ordenacion")
    @Column(name = "ORDENACION")
    protected Integer ordenacion;

    public Integer getOrdenacion() {
        return ordenacion;
    }

    public void setOrdenacion(Integer ordenacion) {
        this.ordenacion = ordenacion;
    }

    @OrderBy("ordenacion")
    public List<Clausula> getClausulas() {
        return clausulas;
    }

    public void setClausulas(List<Clausula> clausulas) {
        this.clausulas = clausulas;
    }

    public Boolean getObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(Boolean obligatoria) {
        this.obligatoria = obligatoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ModeloContrato getModeloContrato() {
        return modeloContrato;
    }

    public void setModeloContrato(ModeloContrato modeloContrato) {
        this.modeloContrato = modeloContrato;
    }

    @Override
    public Object clone() {
        Seccion s_clon = new Seccion();
        s_clon.setDescripcion(this.descripcion);
        s_clon.setModeloContrato(this.modeloContrato);
        s_clon.setNombre(this.getNombre());
        s_clon.setObligatoria(this.obligatoria);
        s_clon.setOrdenacion(this.ordenacion);

        List clausulasClon = new ArrayList();

        for (int j = 0; j < clausulas.size(); j++) {
            Clausula c = clausulas.get(j);
            Clausula c_clon = (Clausula) c.clone();

            c_clon.setSeccion(s_clon);
            clausulasClon.add(c_clon);
        }

        s_clon.setClausulas(clausulasClon);
        return s_clon;
    }

    public static Comparator<Seccion> comparadorOrdenacion = new Comparator<Seccion>(){

        @Override
        public int compare(Seccion o1, Seccion o2) {
            return o1.getOrdenacion().compareTo(o2.getOrdenacion());
        }
    };
}