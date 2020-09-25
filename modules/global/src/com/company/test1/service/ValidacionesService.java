package com.company.test1.service;

import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.enums.*;
import com.company.test1.entity.validaciones.Validacion;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;

import java.util.Date;
import java.util.List;

public interface ValidacionesService {
    String NAME = "test1_ValidacionesService";


    DocumentoImputable gestionaValidacionesImputacionesDocumentoImputableDesdeDocumentoImputable(DocumentoImputable di, boolean resetearEstadosValidacion) throws Exception;
    List<ValidacionImputacionDocumentoImputable> devuelveValidacionesAcordeADatos(DocumentoImputableTipoEnum tdi, ValidacionEstado estado,
                                                                                  Date fechaDesde, Date fechaHasta, String direccion, String nombreProveedorNoDocto,
                                                                                  DepartamentoEstadoEnum estadoDepto, DepartamentoTipoEnum tipoDepartamento,
                                                                                  TipoCiclo tipoCiclo
    ) throws Exception;

    void guardaCambiosEnValidacion(Validacion v);

    ValidacionEstado devuelveEstadoValidacionDocumentoImputable(DocumentoImputable di)
            throws Exception;
}