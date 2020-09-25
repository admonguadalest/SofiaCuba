package com.company.test1.web.screens.datodecontacto;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.DatoDeContacto;

@UiController("test1_DatoDeContacto.edit")
@UiDescriptor("dato-de-contacto-edit.xml")
@EditedEntityContainer("datoDeContactoDc")
@LoadDataBeforeShow
public class DatoDeContactoEdit extends StandardEditor<DatoDeContacto> {
}