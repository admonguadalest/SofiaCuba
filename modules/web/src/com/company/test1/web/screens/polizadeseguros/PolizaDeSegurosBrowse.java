package com.company.test1.web.screens.polizadeseguros;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.PolizaDeSeguros;

@UiController("test1_PolizaDeSeguros.browse")
@UiDescriptor("poliza-de-seguros-browse.xml")
@LookupComponent("polizaDeSegurosesTable")
@LoadDataBeforeShow
public class PolizaDeSegurosBrowse extends StandardLookup<PolizaDeSeguros> {
}