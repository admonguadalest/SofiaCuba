package com.company.test1.web.screens.oferta;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.Oferta;

@UiController("test1_Oferta.edit")
@UiDescriptor("oferta-edit.xml")
@EditedEntityContainer("ofertaDc")
@LoadDataBeforeShow
public class OfertaEdit extends StandardEditor<Oferta> {
}