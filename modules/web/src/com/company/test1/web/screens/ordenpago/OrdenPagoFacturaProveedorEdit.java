package com.company.test1.web.screens.ordenpago;

import com.company.test1.entity.ordenespago.CompensacionOrdenPagoProveedor;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenespago.OrdenPagoFacturaProveedor;
import freemarker.template.SimpleDate;

import javax.inject.Inject;
import java.text.SimpleDateFormat;

@UiController("test1_OrdenPagoFacturaProveedor.edit")
@UiDescriptor("orden-pago-factura-proveedor-edit.xml")
@EditedEntityContainer("ordenPagoFacturaProveedorDc")
@LoadDataBeforeShow
public class OrdenPagoFacturaProveedorEdit extends StandardEditor<OrdenPagoFacturaProveedor> {


    @Inject
    private UiComponents uiComponents;

    public Component getColumnOrdenPagoCompensacion(CompensacionOrdenPagoProveedor copp){
        Label l = uiComponents.create(Label.NAME);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String t = "";
        if (copp.getOrdenPagoProveedor()!=null){
            t = "OPP " + sdf.format(copp.getOrdenPagoProveedor().getFechaValor()) + " " + copp.getImporte();
        }
        if (copp.getOrdenPagoAbono()!=null){
            t = "OPA " + sdf.format(copp.getOrdenPagoAbono().getFechaValor()) + " " + copp.getImporte();
        }
        l.setValue(t);
        return l;
    }
}