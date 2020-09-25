package com.company.test1.web.screens.ordenpago;

import com.haulmont.cuba.gui.components.Form;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenespago.OrdenPagoProveedor;

import javax.inject.Inject;

@UiController("test1_OrdenPagoProveedor.edit")
@UiDescriptor("orden-pago-proveedor-edit.xml")
@EditedEntityContainer("ordenPagoProveedorDc")
@LoadDataBeforeShow
public class OrdenPagoProveedorEdit extends StandardEditor<OrdenPagoProveedor> {
    @Inject
    private InstanceContainer<OrdenPagoProveedor> ordenPagoProveedorDc;
    @Inject
    private Form frm;
    @Inject
    private Label<String> lblInfoPago;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        if (ordenPagoProveedorDc.getItem().getRealizacionPago()!=null){
            frm.setEditable(false);
            lblInfoPago.setValue(ordenPagoProveedorDc.getItem().getRealizacionPago().getIdentificador());
        }else{
            lblInfoPago.setValue("");

        }
    }

    @Subscribe(id = "ordenPagoProveedorDc", target = Target.DATA_CONTAINER)
    private void onOrdenPagoProveedorDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<OrdenPagoProveedor> event) {
        if (event.getProperty().compareTo("importe")==0){
            ordenPagoProveedorDc.getItem().setImporteEfectivo((Double) event.getValue());
        }
    }
    
    
    
    
    
    
    
}