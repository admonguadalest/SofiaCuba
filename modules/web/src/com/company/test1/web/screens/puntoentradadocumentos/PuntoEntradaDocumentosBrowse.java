package com.company.test1.web.screens.puntoentradadocumentos;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.PuntoEntradaDocumentos;

@UiController("test1_PuntoEntradaDocumentos.browse")
@UiDescriptor("punto-entrada-documentos-browse.xml")
@LookupComponent("puntoEntradaDocumentosesTable")
@LoadDataBeforeShow
public class PuntoEntradaDocumentosBrowse extends StandardLookup<PuntoEntradaDocumentos> {
}