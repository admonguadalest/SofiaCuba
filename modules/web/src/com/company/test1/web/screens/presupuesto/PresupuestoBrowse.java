package com.company.test1.web.screens.presupuesto;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosImputables.Presupuesto;

@UiController("test1_Presupuesto.browse")
@UiDescriptor("presupuesto-browse.xml")
@LookupComponent("presupuestoesTable")
@LoadDataBeforeShow
public class PresupuestoBrowse extends StandardLookup<Presupuesto> {
}