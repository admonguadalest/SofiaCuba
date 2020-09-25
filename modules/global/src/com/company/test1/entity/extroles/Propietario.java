package com.company.test1.entity.extroles;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.AsTreeItem;
import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "PROPIETARIO")
@Entity(name = "test1_Propietario")
public class Propietario extends StandardEntity implements AsTreeItem {
    private static final long serialVersionUID = 8675621471784148031L;

    @Length(message = "Longitud de Abreviacion entre 3 y 10 caracteres", min = 3, max = 10)
    @NotNull(message = "Aportar abreviacion de contratos")
    @Column(name = "ABREVIACION_CONTRATOS", length = 25)
    protected String abreviacionContratos;

    @Length(message = "Codigo de Cliente debe ser una cadena entre 3 y 10 caracteres", min = 3, max = 10)
    @NotNull(message = "Aportar codigo de cliente")
    @Column(name = "CODIGO_CLIENTE", length = 50)
    protected String codigoCliente;

    @Column(name = "EXONERACION_IRPF")
    protected Boolean exoneracionIrpf;

    @Column(name = "GESTION_CAJA")
    protected Boolean gestionCaja;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSONA_ID")
    protected Persona persona;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CABECERA_DOCUMENTOS_ARCHIVO_ADJUNTO_ID")
    protected ArchivoAdjunto cabeceraDocumentosArchivoAdjunto;

    @NotNull(message = "Aportar una Cuenta Bancaria")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_BANCARIA_ID")
    protected CuentaBancaria cuentaBancaria;

    @Column(name = "PROSPECTO")
    protected Boolean prospecto;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "propietario")
    protected List<DefinicionRemesa> definicionesRemesa;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public List<DefinicionRemesa> getDefinicionesRemesa() {
        return definicionesRemesa;
    }

    public void setDefinicionesRemesa(List<DefinicionRemesa> definicionesRemesa) {
        this.definicionesRemesa = definicionesRemesa;
    }

    public Boolean getProspecto() {
        return prospecto;
    }

    public void setProspecto(Boolean prospecto) {
        this.prospecto = prospecto;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public ArchivoAdjunto getCabeceraDocumentosArchivoAdjunto() {
        return cabeceraDocumentosArchivoAdjunto;
    }

    public void setCabeceraDocumentosArchivoAdjunto(ArchivoAdjunto cabeceraDocumentosArchivoAdjunto) {
        this.cabeceraDocumentosArchivoAdjunto = cabeceraDocumentosArchivoAdjunto;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Boolean getGestionCaja() {
        return gestionCaja;
    }

    public void setGestionCaja(Boolean gestionCaja) {
        this.gestionCaja = gestionCaja;
    }

    public Boolean getExoneracionIrpf() {
        return exoneracionIrpf;
    }

    public void setExoneracionIrpf(Boolean exoneracionIrpf) {
        this.exoneracionIrpf = exoneracionIrpf;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getAbreviacionContratos() {
        return abreviacionContratos;
    }

    public void setAbreviacionContratos(String abreviacionContratos) {
        this.abreviacionContratos = abreviacionContratos;
    }

    @Override
    public String getTextAsTreeItem() {
        return this.getPersona().getNombreCompleto();
    }
}