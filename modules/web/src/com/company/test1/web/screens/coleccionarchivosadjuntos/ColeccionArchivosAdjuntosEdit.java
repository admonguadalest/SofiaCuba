package com.company.test1.web.screens.coleccionarchivosadjuntos;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ColeccionArchivosAdjuntos;

@UiController("test1_ColeccionArchivosAdjuntos.edit")
@UiDescriptor("coleccion-archivos-adjuntos-edit.xml")
@EditedEntityContainer("coleccionArchivosAdjuntosDc")
@LoadDataBeforeShow
public class ColeccionArchivosAdjuntosEdit extends StandardEditor<ColeccionArchivosAdjuntos> {
}