package com.company.test1.entity.extroles;

import com.company.test1.entity.ArchivoAdjunto;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "COMERCIAL_OFERTAS")
@Entity(name = "test1_ComercialOfertas")
public class ComercialOfertas extends StandardEntity {
    private static final long serialVersionUID = -8422735330183494469L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID")
    protected Proveedor proveedor;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @Lob
    @Column(name = "DETALLE_CORREOS_ELECTRONICOS")
    protected String detalleCorreosElectronicos;

    @Lob
    @Column(name = "DETALLE_NOMBRES")
    protected String detalleNombres;

    @Column(name = "EXCLUIR_ENVIOS")
    protected Boolean excluirEnvios;

    @Column(name = "PUEDE_DAR_ALTA_O_BAJA_AGENTES")
    protected Boolean puedeDarAltaOBajaAgentes;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_ACEPTACION_ID")
    protected ArchivoAdjunto escaneoAceptacion;

    @Lob
    @Column(name = "XML_COMERCIAL_VISITAS")
    protected String xmlComercialVisitas;

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public ArchivoAdjunto getEscaneoAceptacion() {
        return escaneoAceptacion;
    }

    public void setEscaneoAceptacion(ArchivoAdjunto escaneoAceptacion) {
        this.escaneoAceptacion = escaneoAceptacion;
    }

    public String getXmlComercialVisitas() {
        return xmlComercialVisitas;
    }

    public void setXmlComercialVisitas(String xmlComercialVisitas) {
        this.xmlComercialVisitas = xmlComercialVisitas;
    }

    public Boolean getPuedeDarAltaOBajaAgentes() {
        return puedeDarAltaOBajaAgentes;
    }

    public void setPuedeDarAltaOBajaAgentes(Boolean puedeDarAltaOBajaAgentes) {
        this.puedeDarAltaOBajaAgentes = puedeDarAltaOBajaAgentes;
    }

    public Boolean getExcluirEnvios() {
        return excluirEnvios;
    }

    public void setExcluirEnvios(Boolean excluirEnvios) {
        this.excluirEnvios = excluirEnvios;
    }

    public String getDetalleNombres() {
        return detalleNombres;
    }

    public void setDetalleNombres(String detalleNombres) {
        this.detalleNombres = detalleNombres;
    }

    public String getDetalleCorreosElectronicos() {
        return detalleCorreosElectronicos;
    }

    public void setDetalleCorreosElectronicos(String detalleCorreosElectronicos) {
        this.detalleCorreosElectronicos = detalleCorreosElectronicos;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}