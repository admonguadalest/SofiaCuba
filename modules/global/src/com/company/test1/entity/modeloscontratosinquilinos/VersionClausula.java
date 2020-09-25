package com.company.test1.entity.modeloscontratosinquilinos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Table(name = "VERSION_CLAUSULA")
@Entity(name = "test1_VersionClausula")
public class VersionClausula extends StandardEntity {
    private static final long serialVersionUID = 255723166537669249L;

    @NotNull(message = "La version debe estar asociada a una Clausula")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLAUSULA_ID")
    protected Clausula clausula;

    @Column(name = "ES_PREDETERMINADA")
    protected Boolean esPredeterminada;

    @Length(message = "Longitud minima de 1 caracter", min = 1)
    @NotNull(message = "Aportar texto de version")
    @Lob
    @Column(name = "TEXTO_VERSION")
    protected String textoVersion;

    @Lob
    @Column(name = "NOMBRES_PARAMETROS")
    protected String nombresParametros;

    @Column(name = "DESCRIPCION_PARAMETROS")
    protected String descripcionParametros;

    @Lob
    @Column(name = "EXPRESIONES_VALORES_DEFECTO")
    protected String expresionesValoresDefecto;

    @Column(name = "RMI2")
    protected Integer rmi2;

    public Integer getRmi2() {
        return rmi2;
    }

    public void setRmi2(Integer rmi2) {
        this.rmi2 = rmi2;
    }

    public String getExpresionesValoresDefecto() {
        return expresionesValoresDefecto;
    }

    public void setExpresionesValoresDefecto(String expresionesValoresDefecto) {
        this.expresionesValoresDefecto = expresionesValoresDefecto;
    }

    public String getDescripcionParametros() {
        return descripcionParametros;
    }

    public void setDescripcionParametros(String descripcionParametros) {
        this.descripcionParametros = descripcionParametros;
    }

    public String getNombresParametros() {
        return nombresParametros;
    }

    public void setNombresParametros(String nombresParametros) {
        this.nombresParametros = nombresParametros;
    }

    public String getTextoVersion() {
        return textoVersion;
    }

    public void setTextoVersion(String textoVersion) {
        this.textoVersion = textoVersion;
    }

    public Boolean getEsPredeterminada() {
        return esPredeterminada;
    }

    public void setEsPredeterminada(Boolean esPredeterminada) {
        this.esPredeterminada = esPredeterminada;
    }

    public Clausula getClausula() {
        return clausula;
    }

    public void setClausula(Clausula clausula) {
        this.clausula = clausula;
    }


    public static List extraeListaParametros(VersionClausula vc){
        List parametros = new ArrayList();
        String c = vc.getTextoVersion();
        int posDe = 0;int posA;
        posDe = c.indexOf("@[",posDe);
        while(posDe!=-1){
            posA = c.indexOf("]",posDe+2);
            if (posA!=-1){
                String nombreParametro = c.substring(posDe+2,posA);
                parametros.add(nombreParametro);
                posDe = c.indexOf("@[",posDe+1);
            }
        }
        return parametros;

    }
}