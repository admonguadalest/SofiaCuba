package com.company.test1.entity.reportsyplantillas;

import com.company.test1.entity.enums.FlexReportModoProductor;
import com.company.test1.entity.enums.FlexReportTipo;
import com.haulmont.cuba.core.entity.StandardEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "TEST1_FLEX_REPORT")
@Entity(name = "test1_FlexReport")
public class FlexReport extends StandardEntity {
    private static final long serialVersionUID = -8580630707474773994L;

    @Column(name = "FIELDS")
    protected String fields;

    @Column(name = "FORZAR_REPORT_DE_UN_SOLO_REGISTRO_VACIO")
    protected Boolean forzarReportDeUnSoloRegistroVacio;

    @NotNull(message = "Aportar contenido JRXML para Report")
    @Lob
    @Column(name = "CONTENIDO_JRXML")
    protected String contenidoJrxml;

    @Column(name = "MODO_PRODUCTOR")
    protected String modoProductor;

    @Length(message = "Longitud minima de 5 caracteres para campo Nombre", min = 5)
    @NotNull
    @Column(name = "NOMBRE")
    protected String nombre;

    @Lob
    @Column(name = "PARAMETROS_MANUALES")
    protected String parametrosManuales;

    @Lob
    @Column(name = "PRODUCTOR")
    protected String productor;

    @Column(name = "TIPO")
    protected String tipo;

    @Lob
    @Column(name = "PARAMETROS_PRODUCTOR")
    protected String parametrosProductor;

    @Column(name = "RUTA")
    protected String ruta;

    @Column(name = "USER_DATA_SOURCE_CONNECTION")
    protected Boolean userDataSourceConnection;

    public void setModoProductor(FlexReportModoProductor modoProductor) {
        this.modoProductor = modoProductor == null ? null : modoProductor.getId();
    }

    public FlexReportModoProductor getModoProductor() {
        return modoProductor == null ? null : FlexReportModoProductor.fromId(modoProductor);
    }

    public void setTipo(FlexReportTipo tipo) {
        this.tipo = tipo == null ? null : tipo.getId();
    }

    public FlexReportTipo getTipo() {
        return tipo == null ? null : FlexReportTipo.fromId(tipo);
    }

    public Boolean getUserDataSourceConnection() {
        return userDataSourceConnection;
    }

    public void setUserDataSourceConnection(Boolean userDataSourceConnection) {
        this.userDataSourceConnection = userDataSourceConnection;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getParametrosProductor() {
        return parametrosProductor;
    }

    public void setParametrosProductor(String parametrosProductor) {
        this.parametrosProductor = parametrosProductor;
    }

    public String getProductor() {
        return productor;
    }

    public void setProductor(String productor) {
        this.productor = productor;
    }

    public String getParametrosManuales() {
        return parametrosManuales;
    }

    public void setParametrosManuales(String parametrosManuales) {
        this.parametrosManuales = parametrosManuales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenidoJrxml() {
        return contenidoJrxml;
    }

    public void setContenidoJrxml(String contenidoJrxml) {
        this.contenidoJrxml = contenidoJrxml;
    }

    public Boolean getForzarReportDeUnSoloRegistroVacio() {
        return forzarReportDeUnSoloRegistroVacio;
    }

    public void setForzarReportDeUnSoloRegistroVacio(Boolean forzarReportDeUnSoloRegistroVacio) {
        this.forzarReportDeUnSoloRegistroVacio = forzarReportDeUnSoloRegistroVacio;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}