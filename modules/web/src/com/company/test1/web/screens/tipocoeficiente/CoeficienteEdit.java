package com.company.test1.web.screens.tipocoeficiente;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.coeficientes.Coeficiente;

@UiController("test1_Coeficiente.edit")
@UiDescriptor("coeficiente-edit.xml")
@EditedEntityContainer("coeficienteDc")
@LoadDataBeforeShow
public class CoeficienteEdit extends StandardEditor<Coeficiente> {
}