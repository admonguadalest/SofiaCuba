package com.company.test1.web.screens.oferta;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.Oferta;

@UiController("test1_Oferta.browse")
@UiDescriptor("oferta-browse.xml")
@LookupComponent("ofertasTable")
@LoadDataBeforeShow
public class OfertaBrowse extends StandardLookup<Oferta> {
}