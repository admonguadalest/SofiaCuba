package com.company.test1.web.screens.fianza;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.Fianza;

@UiController("test1_Fianza.edit")
@UiDescriptor("fianza-edit.xml")
@EditedEntityContainer("fianzaDc")
@LoadDataBeforeShow
public class FianzaEdit extends StandardEditor<Fianza> {
}