package com.company.test1.web.screens.puntoentradadocumentos;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.PuntoEntradaDocumentos;

@UiController("test1_PuntoEntradaDocumentos.edit")
@UiDescriptor("punto-entrada-documentos-edit.xml")
@EditedEntityContainer("puntoEntradaDocumentosDc")
@LoadDataBeforeShow
public class PuntoEntradaDocumentosEdit extends StandardEditor<PuntoEntradaDocumentos> {
}