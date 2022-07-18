package com.company.test1.web.screens.ordenpago;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenespago.OrdenPagoAbono;

@UiController("test1_OrdenPagoAbono.edit")
@UiDescriptor("orden-pago-abono-edit.xml")
@EditedEntityContainer("ordenPagoAbonoDc")
@LoadDataBeforeShow
public class OrdenPagoAbonoEdit extends StandardEditor<OrdenPagoAbono> {
}