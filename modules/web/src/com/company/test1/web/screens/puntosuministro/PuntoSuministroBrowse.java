package com.company.test1.web.screens.puntosuministro;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.PuntoSuministro;

@UiController("test1_PuntoSuministro.browse")
@UiDescriptor("punto-suministro-browse.xml")
@LookupComponent("puntoSuministroesTable")
@LoadDataBeforeShow
public class PuntoSuministroBrowse extends StandardLookup<PuntoSuministro> {
}