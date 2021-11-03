package com.company.test1.web.screens.notaintervencion;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.NotaIntervencion;

@UiController("test1_NotaIntervencion.browse")
@UiDescriptor("nota-intervencion-browse.xml")
@LookupComponent("notaIntervencionsTable")
@LoadDataBeforeShow
public class NotaIntervencionBrowse extends StandardLookup<NotaIntervencion> {
}