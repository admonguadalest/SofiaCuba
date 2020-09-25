package com.company.test1.web.screens.subrogador;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.Subrogador;

@UiController("test1_Subrogador.edit")
@UiDescriptor("subrogador-edit.xml")
@EditedEntityContainer("subrogadorDc")
@LoadDataBeforeShow
public class SubrogadorEdit extends StandardEditor<Subrogador> {
}