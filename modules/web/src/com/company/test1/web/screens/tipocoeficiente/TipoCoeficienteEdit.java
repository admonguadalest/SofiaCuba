package com.company.test1.web.screens.tipocoeficiente;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.coeficientes.TipoCoeficiente;

@UiController("test1_TipoCoeficiente.edit")
@UiDescriptor("tipo-coeficiente-edit.xml")
@EditedEntityContainer("tipoCoeficienteDc")
@LoadDataBeforeShow
public class TipoCoeficienteEdit extends StandardEditor<TipoCoeficiente> {
}