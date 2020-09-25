package com.company.test1.entity.reportsyplantillas;

import com.company.test1.entity.enums.TipoPlantillaEnum;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamePattern("%s|nombrePlantilla")
@Table(name = "PLANTILLA")
@Entity(name = "test1_Plantilla")
public class Plantilla extends StandardEntity {
    private static final long serialVersionUID = -6319413086718668852L;

    @Length(message = "Longitud Minima de 5 caracteres para campo Nombre", min = 5)
    @NotNull
    @Column(name = "NOMBRE_PLANTILLA")
    protected String nombrePlantilla;

    @Length(message = "Longitud minima de 1 caracter`", min = 1)
    @NotNull(message = "Inserir contenido para Plantilla")
    @Lob
    @Column(name = "CONTENIDO_PLANTILLA")
    protected String contenidoPlantilla;

    @NotNull(message = "Especificar tipo de Plantilla")
    @Column(name = "TIPO_PLANTILLA")
    protected String tipoPlantilla;

    @Column(name = "DE_SISTEMA")
    protected Boolean deSistema;

    @Column(name = "RUTA")
    protected String ruta;

    @Column(name = "RM2ID")
    protected Integer rm2id;


    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public void setTipoPlantilla(TipoPlantillaEnum tipoPlantilla) {
        this.tipoPlantilla = tipoPlantilla == null ? null : tipoPlantilla.getId();
    }

    public TipoPlantillaEnum getTipoPlantilla() {
        return tipoPlantilla == null ? null : TipoPlantillaEnum.fromId(tipoPlantilla);
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Boolean getDeSistema() {
        return deSistema;
    }

    public void setDeSistema(Boolean deSistema) {
        this.deSistema = deSistema;
    }

    public String getContenidoPlantilla() {
        return contenidoPlantilla;
    }

    public void setContenidoPlantilla(String contenidoPlantilla) {
        this.contenidoPlantilla = contenidoPlantilla;
    }

    public String getNombrePlantilla() {
        return nombrePlantilla;
    }

    public void setNombrePlantilla(String nombrePlantilla) {
        this.nombrePlantilla = nombrePlantilla;
    }

    @Transient
    @MetaProperty
    public String getListaParametrosPlantillaLibres(){
        String res = "";
        int currPos0;
        currPos0 = this.contenidoPlantilla.indexOf("@[");
        while(currPos0!=-1){
            int pos1 = this.contenidoPlantilla.indexOf("]",currPos0);
            String nombrePam = this.contenidoPlantilla.substring(currPos0+2,pos1);
            if (nombrePam.indexOf(".")==-1){
                if (res.indexOf(nombrePam)==-1){
                    if (res.trim().length()==0){
                        res += nombrePam;
                    }else{
                        res += ";"+nombrePam;
                    }
                }
            }
            currPos0 = this.contenidoPlantilla.indexOf("@[",currPos0+2);
        }
        return res;

    }
}