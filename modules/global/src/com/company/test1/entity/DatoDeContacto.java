package com.company.test1.entity;

import com.company.test1.entity.enums.TipoDeDatoDeContactoEnum;
import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Table(name = "DATO_DE_CONTACTO")
@Entity(name = "test1_DatoDeContacto")
public class DatoDeContacto extends StandardEntity {
    private static final long serialVersionUID = -8586056989708651343L;

    @NotNull(message = "Aportar Dato")
    @Column(name = "DATO")
    protected String dato;

    @Column(name = "DESCRIPCION_DATO")
    protected String descripcionDato;

    @NotNull(message = "Especificar tipo de dato")
    @Column(name = "TIPO_DE_DATO")
    protected String tipoDeDato;

    @NotNull(message = "el valor de Persona no puede ser nulo")
    @Lookup(type = LookupType.SCREEN)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSONA_ID")
    protected Persona persona;

    @Column(name = "CORREO_DE_RECUPERACION_DE_CONSTRASENYA")
    protected Boolean correoDeRecuperacionDeConstrasenya;

    public Boolean getCorreoDeRecuperacionDeConstrasenya() {
        return correoDeRecuperacionDeConstrasenya;
    }

    public void setCorreoDeRecuperacionDeConstrasenya(Boolean correoDeRecuperacionDeConstrasenya) {
        this.correoDeRecuperacionDeConstrasenya = correoDeRecuperacionDeConstrasenya;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public TipoDeDatoDeContactoEnum getTipoDeDato() {
        return tipoDeDato == null ? null : TipoDeDatoDeContactoEnum.fromId(tipoDeDato);
    }

    public void setTipoDeDato(TipoDeDatoDeContactoEnum tipoDeDato) {
        this.tipoDeDato = tipoDeDato == null ? null : tipoDeDato.getId();
    }

    public String getDescripcionDato() {
        return descripcionDato;
    }

    public void setDescripcionDato(String descripcionDato) {
        this.descripcionDato = descripcionDato;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public static List<DatoDeContacto> getDatosDeContactoParaTipo(Persona p, TipoDeDatoDeContactoEnum tipo){
        ArrayList<DatoDeContacto> al = new ArrayList<DatoDeContacto>();
        for (int i = 0; i < p.getDatosDeContacto().size(); i++) {
            DatoDeContacto ddc = p.getDatosDeContacto().get(i);
            if (ddc.getTipoDeDato()==tipo){
                al.add(ddc);
            }
        }

        return al;
    }

    public static String getTelefonosDeContacto(Persona p){

        List<DatoDeContacto> telefonosFijos = getDatosDeContactoDeTipoDeterminado(p, TipoDeDatoDeContactoEnum.TELEFONO);
        List<DatoDeContacto> telefonosMoviles = getDatosDeContactoDeTipoDeterminado( p, TipoDeDatoDeContactoEnum.MOBIL);

        List ddc = new ArrayList();
        for (int i = 0; i < telefonosFijos.size(); i++) {
            ddc.add(telefonosFijos.get(i));
        }
        for (int i = 0; i < telefonosMoviles.size(); i++) {
            ddc.add(telefonosMoviles.get(i));
        }

        return getDatosDeContactoAString(ddc);
    }

    public static List<DatoDeContacto> getDatosDeContactoDeTipoDeterminado(Persona p, TipoDeDatoDeContactoEnum tipoDato){
        List<DatoDeContacto> l = p.getDatosDeContacto();
        List<DatoDeContacto> datosDeContactoARetornar = new ArrayList<DatoDeContacto>();

        for (int i = 0; i < l.size(); i++) {
            DatoDeContacto ddc = l.get(i);
            if (ddc.getTipoDeDato().compareTo(tipoDato) == 0){
                datosDeContactoARetornar.add(ddc);
            }

        }
        return datosDeContactoARetornar;
    }

    public static String  getDatosDeContactoAString(List<DatoDeContacto> listaDDC){

        if (listaDDC.isEmpty()) return "";

        String s = "";

        for (int i = 0; i < listaDDC.size(); i++) {
            DatoDeContacto ddc = listaDDC.get(i);
            s += ddc.getDato() + " / ";
        }

        s = s.substring(0,s.length()-3);

        return s;
    }


}