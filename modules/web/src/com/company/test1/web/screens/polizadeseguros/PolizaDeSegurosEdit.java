package com.company.test1.web.screens.polizadeseguros;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.PolizaDeSeguros;

@UiController("test1_PolizaDeSeguros.edit")
@UiDescriptor("poliza-de-seguros-edit.xml")
@EditedEntityContainer("polizaDeSegurosDc")
@LoadDataBeforeShow
public class PolizaDeSegurosEdit extends StandardEditor<PolizaDeSeguros> {
}