package com.company.test1.web.screens.comercialofertas;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.extroles.ComercialOfertas;

@UiController("test1_ComercialOfertas.browse")
@UiDescriptor("comercial-ofertas-browse.xml")
@LookupComponent("comercialOfertasesTable")
@LoadDataBeforeShow
public class ComercialOfertasBrowse extends StandardLookup<ComercialOfertas> {
}