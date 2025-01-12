package com.company.test1.entity.recibos;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.Comparator;
import java.util.Date;

@MetaClass(name = "test1_ReciboReport")
public class ReciboReport extends BaseUuidEntity {
    private static final long serialVersionUID = -6242104061380286327L;

    @MetaProperty
    private String ubicacion;

    @MetaProperty
    private String piso;

    @MetaProperty
    private String puerta;

    @MetaProperty
    private String nombreInquilino;

    @MetaProperty
    private Date fechaEmision;

    @MetaProperty
    private Double importe;

    @MetaProperty
    private Double cuotaIva;

    @MetaProperty
    private Double cuotaIrpf;

    @MetaProperty
    private Double importeFinal;

    @MetaProperty
    private String ampliacion;

    @MetaProperty
    private String reciboId;

    @MetaProperty
    private String remesa;

    @MetaProperty
    private String numRecibo;

    @MetaProperty
    private String propietario;

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public void setImporteFinal(Double importeFinal) {
        this.importeFinal = importeFinal;
    }

    public Double getImporteFinal() {
        return importeFinal;
    }

    public String getNumRecibo() {
        return numRecibo;
    }

    public void setNumRecibo(String numRecibo) {
        this.numRecibo = numRecibo;
    }

    public String getRemesa() {
        return remesa;
    }

    public void setRemesa(String remesa) {
        this.remesa = remesa;
    }

    public String getReciboId() {
        return reciboId;
    }

    public void setReciboId(String reciboId) {
        this.reciboId = reciboId;
    }

    public String getAmpliacion() {
        return ampliacion;
    }

    public void setAmpliacion(String ampliacion) {
        this.ampliacion = ampliacion;
    }

    public Double getCuotaIrpf() {
        return cuotaIrpf;
    }

    public void setCuotaIrpf(Double cuotaIrpf) {
        this.cuotaIrpf = cuotaIrpf;
    }

    public Double getCuotaIva() {
        return cuotaIva;
    }

    public void setCuotaIva(Double cuotaIva) {
        this.cuotaIva = cuotaIva;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getNombreInquilino() {
        return nombreInquilino;
    }

    public void setNombreInquilino(String nombreInquilino) {
        this.nombreInquilino = nombreInquilino;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public static Comparator getNaturalComparator(){
        Comparator<ReciboReport> c = new Comparator<ReciboReport>() {
            @Override
            public int compare(ReciboReport o1, ReciboReport o2) {
                if (o1.getUbicacion().compareTo(o2.getUbicacion())==0){
                    if (o1.getPiso().compareTo(o2.getPiso())==0){
                        return o1.getPuerta().compareTo(o2.getPuerta());
                    }else{
                        return o1.getPiso().compareTo(o2.getPiso());
                    }
                }else{
                    return o1.getUbicacion().compareTo(o2.getUbicacion());
                }
            }

        };
        return c;
    }
}