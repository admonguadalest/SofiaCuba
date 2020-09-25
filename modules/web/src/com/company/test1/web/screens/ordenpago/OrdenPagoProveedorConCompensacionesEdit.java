package com.company.test1.web.screens.ordenpago;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenespago.OrdenPagoProveedor;

@UiController("test1_OrdenPagoProveedorConCompensaciones.edit")
@UiDescriptor("orden-pago-proveedor-con-compensaciones-edit.xml")
@EditedEntityContainer("ordenPagoProveedorDc")
@LoadDataBeforeShow
public class OrdenPagoProveedorConCompensacionesEdit extends StandardEditor<OrdenPagoProveedor> {
}