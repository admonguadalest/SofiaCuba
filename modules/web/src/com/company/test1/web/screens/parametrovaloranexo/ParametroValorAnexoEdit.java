package com.company.test1.web.screens.parametrovaloranexo;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.ParametroValorAnexo;

@UiController("test1_ParametroValorAnexo.edit")
@UiDescriptor("parametro-valor-anexo-edit.xml")
@EditedEntityContainer("parametroValorAnexoDc")
@LoadDataBeforeShow
public class ParametroValorAnexoEdit extends StandardEditor<ParametroValorAnexo> {
}