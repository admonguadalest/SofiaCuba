package com.company.test1.web.screens.contrsenas;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.RegistroContrasena;

@UiController("test1_RegistroContrasena.edit")
@UiDescriptor("registro-contrasena-edit.xml")
@EditedEntityContainer("registroContrasenaDc")
@LoadDataBeforeShow
public class RegistroContrasenaEdit extends StandardEditor<RegistroContrasena> {
}