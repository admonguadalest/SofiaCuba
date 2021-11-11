package com.company.test1.entity.extroles;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.enums.ModoPagoProveedor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
/*
PENDIENTE: LA COLUMNA modoDePago SE HA DE ELIMINAR, PUES HABILITO BOOLEANOS PARA REGISTRAR LOS MODOS DE PAGO ADMISIBLES. DE LA MISMA MANERA
LA ENUMERACION ModoDePagoEnum SE DEBERÍA ELIMINAR PUES YA NO SIRVE A NINGUN PROPOSITO
 */


@Table(name = "PROVEEDOR")
@Entity(name = "test1_Proveedor")
public class Proveedor extends StandardEntity {
    private static final long serialVersionUID = -9118018409921583374L;

    @NotNull(message = "Proveedor debe tener una persona asociada")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PERSONA_ID")
    protected Persona persona;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "proveedor")
    protected List<ProgramacionConceptoAdicional> programacionesConceptosAdicionales = new ArrayList<ProgramacionConceptoAdicional>();

    @NotNull(message = "Proveer descripcion de actividad")
    @Column(name = "DESCRIPCION_ACTIVIDAD")
    protected String descripcionActividad;

    @Column(name = "OBSERVACIONES")
    protected String observaciones;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "proveedor")
    protected ComercialOfertas comercialOfertas;

    @NotNull(message = "Aportar un Nombre Comercial")
    @Column(name = "NOMBRE_COMERCIAL")
    protected String nombreComercial;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_BANCARIA_ID")
    protected CuentaBancaria cuentaBancaria;

    @Column(name = "MODO_DE_PAGO")
    protected String modoDePago;

    @Column(name = "MODO_DE_PAGO_TELEMATICO")
    protected Boolean modoDePagoTelematico = false;

    @Column(name = "MODO_DE_PAGO_DOMICILIADO")
    protected Boolean modoDePagoDomiciliado = false;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_BANCARIA_DOMICILIADO_ID")
    protected CuentaBancaria cuentaBancariaDomiciliado;

    @Column(name = "ENVIAR_CORREO_CONFIRMACION_AL_APROBAR_FACTURA")
    protected Boolean enviarCorreoConfirmacionAlAprobarFactura;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public CuentaBancaria getCuentaBancariaDomiciliado() {
        return cuentaBancariaDomiciliado;
    }

    public void setCuentaBancariaDomiciliado(CuentaBancaria cuentaBancariaDomiciliado) {
        this.cuentaBancariaDomiciliado = cuentaBancariaDomiciliado;
    }

    public Boolean getModoDePagoDomiciliado() {
        return modoDePagoDomiciliado;
    }

    public void setModoDePagoDomiciliado(Boolean modoDePagoDomiciliado) {
        this.modoDePagoDomiciliado = modoDePagoDomiciliado;
    }

    public Boolean getModoDePagoTelematico() {
        return modoDePagoTelematico;
    }

    public void setModoDePagoTelematico(Boolean modoDePagoTelematico) {
        this.modoDePagoTelematico = modoDePagoTelematico;
    }

    public void setModoDePago(ModoPagoProveedor modoDePago) {
        this.modoDePago = modoDePago == null ? null : modoDePago.getId();
    }

    public ModoPagoProveedor getModoDePago() {
        return modoDePago == null ? null : ModoPagoProveedor.fromId(modoDePago);
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public List<ProgramacionConceptoAdicional> getProgramacionesConceptosAdicionales() {
        return programacionesConceptosAdicionales;
    }

    public void setProgramacionesConceptosAdicionales(List<ProgramacionConceptoAdicional> programacionesConceptosAdicionales) {
        this.programacionesConceptosAdicionales = programacionesConceptosAdicionales;
    }

    public Boolean getEnviarCorreoConfirmacionAlAprobarFactura() {
        return enviarCorreoConfirmacionAlAprobarFactura;
    }

    public void setEnviarCorreoConfirmacionAlAprobarFactura(Boolean enviarCorreoConfirmacionAlAprobarFactura) {
        this.enviarCorreoConfirmacionAlAprobarFactura = enviarCorreoConfirmacionAlAprobarFactura;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public ComercialOfertas getComercialOfertas() {
        return comercialOfertas;
    }

    public void setComercialOfertas(ComercialOfertas comercialOfertas) {
        this.comercialOfertas = comercialOfertas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}