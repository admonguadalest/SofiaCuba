package com.company.test1.web.screens.propietario;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.extroles.Propietario;

@UiController("test1_Propietario.edit")
@UiDescriptor("propietario-edit.xml")
@EditedEntityContainer("propietarioDc")
@LoadDataBeforeShow
public class PropietarioEdit extends StandardEditor<Propietario> {
}