package com.company.test1.web.screens.contrsenas;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.RegistroContrasena;

@UiController("test1_RegistroContrasena.browse")
@UiDescriptor("registro-contrasena-browse.xml")
@LookupComponent("registroContrasenasTable")
@LoadDataBeforeShow
public class RegistroContrasenaBrowse extends StandardLookup<RegistroContrasena> {



}