package com.company.test1.web.screens.propietario;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.extroles.Propietario;

@UiController("test1_Propietario.browse")
@UiDescriptor("propietario-browse.xml")
@LookupComponent("propietariosTable")
@LoadDataBeforeShow
public class PropietarioBrowse extends StandardLookup<Propietario> {
}