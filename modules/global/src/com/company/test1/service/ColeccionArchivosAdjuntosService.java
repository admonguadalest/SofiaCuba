package com.company.test1.service;

import com.company.test1.entity.*;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;

import java.util.List;

public interface ColeccionArchivosAdjuntosService {
    String NAME = "ColeccionArchivosAdjuntosService";

    List<ColeccionArchivosAdjuntos> devuelveListColeccionesIntegrantes(ColeccionArchivosAdjuntos caa);

    ArchivoAdjuntoExt getArchivoAdjuntoExt(ArchivoAdjunto aa);

    ArchivoAdjuntoExt generaNuevoArchivoAdjuntoExt(ArchivoAdjunto aa) throws Exception;

    FotosThumbnailExt getFotoDocumentoFotograficoExt(FotoDocumentoFotografico fdf);

    FotosDocumentosFotograficosExt generaNuevoFotoDocumentoFotograficoExt(FotoDocumentoFotografico aa) throws Exception;

    FotosThumbnailExt generaNuevoFotoThumbnailExt(FotoThumbnail aa) throws Exception;
}