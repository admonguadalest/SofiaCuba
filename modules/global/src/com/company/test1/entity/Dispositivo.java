package com.company.test1.entity;

import com.company.test1.entity.departamentos.Departamento;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TEST1_DISPOSITIVO")
@Entity(name = "test1_Dispositivo")
public class Dispositivo extends StandardEntity {
    private static final long serialVersionUID = 3826830744019377976L;

    @Column(name = "IDENTIFICADOR")
    protected String identificador;

    @Column(name = "TIPO_DISPOSITIVO")
    protected String tipoDispositivo;

    @Lob
    @Column(name = "AMPLIACION")
    protected String ampliacion;

    @Column(name = "INSTALADO_EN")
    protected String instaladoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTAMENTO_ID")
    protected Departamento departamento;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISPOSITIVO_PADRE_ID")
    protected Dispositivo dispositivoPadre;

    public Dispositivo getDispositivoPadre() {
        return dispositivoPadre;
    }

    public void setDispositivoPadre(Dispositivo dispositivoPadre) {
        this.dispositivoPadre = dispositivoPadre;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getInstaladoEn() {
        return instaladoEn;
    }

    public void setInstaladoEn(String instaladoEn) {
        this.instaladoEn = instaladoEn;
    }

    public String getAmpliacion() {
        return ampliacion;
    }

    public void setAmpliacion(String ampliacion) {
        this.ampliacion = ampliacion;
    }

    public TipoDispositivo getTipoDispositivo() {
        return tipoDispositivo == null ? null : TipoDispositivo.fromId(tipoDispositivo);
    }

    public void setTipoDispositivo(TipoDispositivo tipoDispositivo) {
        this.tipoDispositivo = tipoDispositivo == null ? null : tipoDispositivo.getId();
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}