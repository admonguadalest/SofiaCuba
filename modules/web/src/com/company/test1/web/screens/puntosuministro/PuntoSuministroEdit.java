package com.company.test1.web.screens.puntosuministro;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.PuntoSuministro;

@UiController("test1_PuntoSuministro.edit")
@UiDescriptor("punto-suministro-edit.xml")
@EditedEntityContainer("puntoSuministroDc")
@LoadDataBeforeShow
public class PuntoSuministroEdit extends StandardEditor<PuntoSuministro> {
}