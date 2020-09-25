package com.company.test1.web.screens.contratoinquilino;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.ParametroValor;

@UiController("test1_ParametroValor.edit")
@UiDescriptor("parametro-valor-edit.xml")
@EditedEntityContainer("parametroValorDc")
@LoadDataBeforeShow
public class ParametroValorEdit extends StandardEditor<ParametroValor> {
}