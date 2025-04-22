package com.company.test1.service;

import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.enums.*;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.validaciones.Validacion;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ValidacionesService {
    String NAME = "test1_ValidacionesService";


    DocumentoImputable gestionaValidacionesImputacionesDocumentoImputableDesdeDocumentoImputable(DocumentoImputable di, boolean resetearEstadosValidacion) throws Exception;
    List<ValidacionImputacionDocumentoImputable> devuelveValidacionesAcordeADatos(DocumentoImputableTipoEnum tdi, ValidacionEstado estado,
                                                                                  Date fechaDesde, Date fechaHasta, String direccion, String nombreProveedorNoDocto,
                                                                                  DepartamentoEstadoEnum estadoDepto, DepartamentoTipoEnum tipoDepartamento,
                                                                                  TipoCiclo tipoCiclo, String nombreTitular
    ) throws Exception;

    void guardaCambiosEnValidacion(Validacion v);

    ValidacionEstado devuelveEstadoValidacionDocumentoImputable(DocumentoImputable di)
            throws Exception;

    List<Proveedor> devuelveIdsProveedoresConValidacionesPagoPendientes(java.util.Date fechaDesde) throws Exception;
}