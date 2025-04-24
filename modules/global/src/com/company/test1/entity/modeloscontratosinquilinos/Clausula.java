package com.company.test1.entity.modeloscontratosinquilinos;

import com.company.test1.entity.contratosinquilinos.ImplementacionModelo;
import com.company.test1.entity.contratosinquilinos.OverrideClausula;
import com.company.test1.validations.modeloscontratosinquilinos.ClausulaBean;
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
import java.util.List;

@Table(name = "CLAUSULA")
@Entity(name = "test1_Clausula")
@ClausulaBean(groups = UiCrossFieldChecks.class)
public class Clausula extends StandardEntity {
    private static final long serialVersionUID = -7972590871129039805L;

    @NotNull(message = "La Clausula debe estar asociada a una Seccion")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECCION_ID")
    protected Seccion seccion;

    @Length(message = "Aportar un nombre para Clausula de al menos 3 caracteres", min = 3)
    @NotNull
    @Column(name = "NOMBRE_CLAUSULA")
    protected String nombreClausula;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Positive
    @NotNull
    @Column(name = "ORDENACION")
    protected Integer ordenacion;

    @Size(message = "Aportar al menos una version de clausula", min = 1)
    @NotNull
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "clausula")
    protected List<VersionClausula> versiones = new ArrayList<VersionClausula>();

    @Column(name = "RMI2")
    protected Integer rmi2;

    public Integer getRmi2() {
        return rmi2;
    }

    public void setRmi2(Integer rmi2) {
        this.rmi2 = rmi2;
    }

    public List<VersionClausula> getVersiones() {
        return versiones;
    }

    public void setVersiones(List<VersionClausula> versiones) {
        this.versiones = versiones;
    }

    public Integer getOrdenacion() {
        return ordenacion;
    }

    public void setOrdenacion(Integer ordenacion) {
        this.ordenacion = ordenacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreClausula() {
        return nombreClausula;
    }

    public void setNombreClausula(String nombreClausula) {
        this.nombreClausula = nombreClausula;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    @Override
    public Object clone(){

        Clausula c_clon = new Clausula();

        c_clon.setDescripcion(this.getDescripcion());
        c_clon.setNombreClausula(this.getNombreClausula());
        c_clon.setOrdenacion(this.getOrdenacion());
        c_clon.setSeccion(this.seccion);
        List versionesClon = new ArrayList();

        for (int k = 0; k < this.getVersiones().size(); k++) {
            VersionClausula vc = this.getVersiones().get(k);
            VersionClausula vc_clon = new VersionClausula();
            vc_clon.setClausula(c_clon);
            vc_clon.setDescripcionParametros(vc.getDescripcionParametros());
            vc_clon.setEsPredeterminada(vc.getEsPredeterminada());
            vc_clon.setExpresionesValoresDefecto(vc.getExpresionesValoresDefecto());
            vc_clon.setNombresParametros(vc.getNombresParametros());
            vc_clon.setTextoVersion(vc.getTextoVersion());

            vc_clon.setClausula(c_clon);
            versionesClon.add(vc_clon);
        }

        c_clon.setVersiones(versionesClon);

        return c_clon;
    }

    public static VersionClausula getVersionPredeterminada(Clausula c) throws Exception{
        List<VersionClausula> l = c.getVersiones();
        for (int i = 0; i < l.size(); i++) {
            VersionClausula vc = l.get(i);
            if (vc.getEsPredeterminada()==null){
                vc.setEsPredeterminada(false);
            }
            if (vc.getEsPredeterminada()){
                return vc;
            }
        }
        //cancelamos el lanzamiento de esta excepcion hasta haber puesto una regla de validacion en
        //modelos de contrato. De momento devolvemos la primera version. Si no hay ninguna entonces si
        //lanzamos una excepcion
        //throw new Exception("No se hallo version predeterminada para la clausula " + c.getNombreClausula());
        if (l.size()==0){
            throw new Exception("No se hallaron versiones para la clausula " + c.getNombreClausula());
        }else{
            return l.get(0);
        }
    }

    public static List<Clausula> getTodasLasClausulasDeModeloContrato(ModeloContrato mc){
        ArrayList al = new ArrayList();
        for (int i = 0; i < mc.getSecciones().size(); i++) {
            Seccion s = mc.getSecciones().get(i);
            for (int j = 0; j < s.getClausulas().size(); j++) {
                Clausula c = s.getClausulas().get(j);
                al.add(c);
            }
        }
        return al;
    }

    public static VersionClausula getVersionClausulaAplicada(ImplementacionModelo im, Clausula c) throws Exception{
        for (int i = 0; i < im.getOverrideClausulas().size(); i++) {
            //ArrayList al = (ArrayList) this.overrideClausulas.get(i);
            OverrideClausula oc = im.getOverrideClausulas().get(i);
            Clausula c_ = oc.getClausula();
            if (c.getId().compareTo(c_.getId())==0){
                VersionClausula vc = oc.getVersionAplicada();
                return vc;
            }

        }
        return Clausula.getVersionPredeterminada(c);
    }


}