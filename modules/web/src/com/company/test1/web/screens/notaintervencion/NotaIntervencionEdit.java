package com.company.test1.web.screens.notaintervencion;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.NotaIntervencion;

@UiController("test1_NotaIntervencion.edit")
@UiDescriptor("nota-intervencion-edit.xml")
@EditedEntityContainer("notaIntervencionDc")
@LoadDataBeforeShow
public class NotaIntervencionEdit extends StandardEditor<NotaIntervencion> {
}