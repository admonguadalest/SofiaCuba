package com.company.test1.service.accessory;


import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 *
 * @author Xavier
 */
public class HelperReportValidacionesImputacionDocumentoImputable {

    ValidacionImputacionDocumentoImputable vidi;

    String fechaEmision;
    String departamento;
    String proveedor;
    String fechaSolicitud;
    String estado;
    String fechaAprovacion;
    String descripcion;
    String observaciones;
    String importe;

    public HelperReportValidacionesImputacionDocumentoImputable(ValidacionImputacionDocumentoImputable vidi) {
        this.vidi = vidi;
    }



    public String getDepartamento() {
        return vidi.getImputacionDocumentoImputable().getCiclo().getDepartamento().getNombreDescriptivoCompleto();
    }

    public String getDescripcion() {
        return vidi.getImputacionDocumentoImputable().getDescripcionImputacion();
    }

    public String getEstado() {
        return  vidi.getEstadoValidacion().getId().toString();
    }

    public String getFechaAprovacion() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (vidi.getFechaAprobacionRechazo() == null) {
            return null;
        }

        String fechaAprovacionNombre = sdf.format(vidi.getFechaAprobacionRechazo());

        return fechaAprovacionNombre;
    }

    public String getFechaEmision() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaEmisionNombre = sdf.format(vidi.getImputacionDocumentoImputable().getDocumentoImputable().getFechaEmision());

        return fechaEmisionNombre;
    }

    public String getFechaSolicitud() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaCreacionNombre = sdf.format(vidi.getCreateTs());


        return fechaCreacionNombre;
    }

    public String getImporte() {

        Double importeImputacion = vidi.getImputacionDocumentoImputable().getImporteImputacion();

        if (importeImputacion==null) return "N/D";

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String importeNombre = nf.format(importeImputacion);

        return importeNombre;
    }

    public String getObservaciones() {
        return vidi.getImputacionDocumentoImputable().getDocumentoImputable().getObservaciones();
    }

    public String getProveedor() {
        DocumentoProveedor prov = (DocumentoProveedor) vidi.getImputacionDocumentoImputable().getDocumentoImputable();
        return prov.getNombreProveedor();
    }

    public String getNumDocumento() {
        return vidi.getImputacionDocumentoImputable().getDocumentoImputable().getNumDocumento();
    }
}